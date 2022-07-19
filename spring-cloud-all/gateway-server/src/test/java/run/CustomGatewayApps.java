package run;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClient;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerHystrixFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.WebFilter;
import utils.Utils;

import javax.validation.constraints.AssertTrue;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_HANDLER_MAPPER_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Data
@Slf4j
@SpringBootTest(classes = CustomGatewayApps.Dummy.class,properties = {"server.port=9090","management.server.port=9091"}
        ,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CustomGatewayApps {

    @Autowired
    private GatewayProperties gatewayProperties;
    @Autowired
    private ListInfo listInfo;

    static WebTestClient buildSimple;
    static WebTestClient build;
    static int  managementPort =0;
    @Autowired
    GenericApplicationContext applicationContext;
    @Autowired
    DiscoveryClient discoveryClient;



    // todo pro: don't load the yourself 's property.

    @BeforeAll
    public static void setUp() {
        buildSimple = WebTestClient.bindToServer().baseUrl("http://localhost:9090/").responseTimeout(Duration.ofHours(1)).build();
    }
    Object lock = new Object();

    /**
     * use the metric and others to look the bean .to locate the pro.
     */
    @Autowired
    private RestTemplate restTemplate;
    @Test
    public void loadBalancerClientToRequestOtherThanRoute(){
        // plain/text
        String uri="http://gateway/actuator/env";
        String env = restTemplate.getForObject(uri, String.class);
        log.info("{}",env);
        Assertions.assertTrue(env.contains("\"activeProfiles\":[\"peer2\"]"));

    }
    @Autowired
    List<WebFilter> webFilterList;
    @Autowired
    List<HandlerMapping> handlerMappings;

    @Autowired
    ReactiveResilience4JCircuitBreakerFactory circuitBreakerFactory;
    private void checkConfigForCircuitBreaker(){
        CircuitBreakerRegistry circuitBreakerRegistry = circuitBreakerFactory.getCircuitBreakerRegistry();
        TimeLimiterRegistry timeLimiterRegistry = circuitBreakerFactory.getTimeLimiterRegistry();
        TimeLimiter backendA = timeLimiterRegistry.timeLimiter("backendA");
        Assertions.assertEquals(backendA.getTimeLimiterConfig().getTimeoutDuration(),Duration.ofSeconds(11) );;
        CircuitBreaker aDefault = circuitBreakerRegistry.circuitBreaker("default");
        Duration maxWaitDurationInHalfOpenState = aDefault.getCircuitBreakerConfig().getWaitDurationInOpenState();
        Assertions.assertEquals(maxWaitDurationInHalfOpenState,Duration.ofSeconds(10));
    }

    @Test
    public void startUp() throws InterruptedException {
        checkConfigForCircuitBreaker();;

        try {
            Class.forName("io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator");
        } catch (ClassNotFoundException e) {
            log.error("{}, class not found","io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator");
        }
        Utils.print(applicationContext);
        webFilterList.stream().forEach(webFilter -> {
            log.info("{}",webFilter.toString());
        });
        log.info("==========================================");
        handlerMappings.stream().forEach(handlerMapping -> {
            log.info("{}",handlerMapping.toString());
        });
        synchronized (lock){lock.wait();}
    }
    @Test
    public void getSimple() throws InterruptedException {

        buildSimple.get().uri("/get").header("Host","www.readbody.org").exchange().expectBody().consumeWith((EntityExchangeResult<byte[]> result) -> {
            HttpHeaders responseHeaders = result.getResponseHeaders();
            byte[] responseBody = result.getResponseBody();
            String s = new String(responseBody);
            log.info("res:{}", s);
        });
    }
    @Test
    public void get4(){
        buildSimple.post().uri("/post").header("Host","www.myhost.org").exchange().expectBody(Map.class).consumeWith((EntityExchangeResult<Map> result)->{
            Map responseBody = result.getResponseBody();
            Assertions.assertEquals(responseBody.get("url"),"http://www.myhost.org/post");
            log.info("host:{}",responseBody);
        });
    }
    @Test
    public void rewrite(){
        buildSimple.post().uri("/foo/post").header("Host","www.rewrite.org").exchange().expectBody(Map.class).consumeWith((EntityExchangeResult<Map> result)->{
            Map responseBody  = result.getResponseBody();
            Assertions.assertEquals(responseBody.get("url"),"http://www.rewrite.org/post");
            log.info("rewrite:{}",responseBody);
        });
    }

//    @Disabled
    @Test
    public void get3() {
        System.out.println(listInfo);
    }

    @Disabled
    @Test
    public void get() {
        List<FilterDefinition> defaultFilters = gatewayProperties.getDefaultFilters();
        defaultFilters.stream().forEach(defaultFilter -> {
            Map<String, String> args = defaultFilter.getArgs();
            args.entrySet().stream().forEach((kv) -> {
                String key = kv.getKey();
                String value = kv.getValue();
                log.info("key:{},val:{}", key, value);
            });
            String name = defaultFilter.getName();
            log.info("name:{}", name);
        });
    }

    private void verfiyBySendRequest() {

    }
    @Disabled
    @Test
    public void makeUpTheRoute() {

    }

    @Import(com.SpringBootGatewayApps.class)
    @EnableAutoConfiguration
    @Configuration
    static class Dummy {
        protected static final String HANDLER_MAPPER_HEADER = "X-Gateway-Handler-Mapper-Class";

        protected static final String ROUTE_ID_HEADER = "X-Gateway-RouteDefinition-Id";
        @Bean
        public ListInfo listInfo() {
            return new ListInfo();
        }

        @Order(500)
        @Bean
        public GatewayFilter captureResponse(){
            return  (exchange, chain) -> { // GATEWAY_PREDICATE_ROUTE_ATTR
                String value = exchange.getAttributeOrDefault(GATEWAY_HANDLER_MAPPER_ATTR,
                        "N/A");
                if (!exchange.getResponse().isCommitted()) {
                    exchange.getResponse().getHeaders().add(HANDLER_MAPPER_HEADER, value);
                }
                Route route = exchange.getAttributeOrDefault(GATEWAY_ROUTE_ATTR, null);
                if (route != null) {
                    if (!exchange.getResponse().isCommitted()) {
                        exchange.getResponse().getHeaders().add(ROUTE_ID_HEADER,
                                route.getId());
                    }
                }
                return chain.filter(exchange);
            };
        }
        @Bean
        public WebClient webClient(){
            WebClient build = WebClient.builder().baseUrl("http://172.168.1.73:9090/").build();
            return build;
        }
        @Bean
        public Customizer<ReactiveResilience4JCircuitBreakerFactory> slowCoustomzier(){

            return factory->{
                factory.addCircuitBreakerCustomizer(circuitBreaker -> {circuitBreaker.transitionToForcedOpenState(); },"slowcmd");
            };
        }
    }

    @Data
    @ConfigurationProperties(prefix = "listinfo")
    static class ListInfo {

        private List<Info> list;
        private ObjectS  objectS;
    }
    @NoArgsConstructor
    @ToString
    @AllArgsConstructor
    static class ObjectS{
        private int age;
        private String name;
    }
    @Data
    static class Info {
        private List<String> list;
        private List<SpringCloudCircuitBreakerFilterFactory.Config> circuitBreakerList;
        private Map<String, String> map;
        private String name;
    }
}
