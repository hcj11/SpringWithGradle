package run;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.config.GatewayResilience4JCircuitBreakerAutoConfiguration;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.GatewayFilter;
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
import utils.Utils;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_HANDLER_MAPPER_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Data
@Slf4j
@SpringBootTest(classes = CustomResilience4jCircuitBreakerFactory.Dummy.class,properties = {"server.port=9090","management.server.port=9091"}
        ,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CustomResilience4jCircuitBreakerFactory {

    @Autowired
    private GatewayProperties gatewayProperties;
    static WebTestClient buildSimple;
    static WebTestClient build;
    static int  managementPort =0;
    @Autowired
    GenericApplicationContext applicationContext;




    @BeforeAll
    public static void setUp() {
        buildSimple = WebTestClient.bindToServer().baseUrl("http://localhost:9090/").responseTimeout(Duration.ofHours(1)).build();
    }
    Object lock = new Object();

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void startUp() throws InterruptedException {
        Utils.print(applicationContext);
        synchronized (lock){lock.wait();}
    }


    @Import(com.SpringBootGatewayApps.class)
    @EnableAutoConfiguration
    @ImportAutoConfiguration(GatewayResilience4JCircuitBreakerAutoConfiguration.class)
    @Configuration
    static class Dummy {
        protected static final String HANDLER_MAPPER_HEADER = "X-Gateway-Handler-Mapper-Class";

        protected static final String ROUTE_ID_HEADER = "X-Gateway-RouteDefinition-Id";

        @Order(500)
        @Bean
        public GatewayFilter captureResponse(){
            return  (exchange, chain) -> {
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
        public Customizer<Resilience4JCircuitBreakerFactory> slowCoustomzier(){
            return factory->{
                factory.addCircuitBreakerCustomizer(circuitBreaker -> {circuitBreaker.transitionToForcedOpenState();},"slowcmd");
            };

        }
    }


}
