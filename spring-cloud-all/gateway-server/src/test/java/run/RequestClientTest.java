package run;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.TimeoutException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import utils.Utils;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_HANDLER_MAPPER_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@Data
public class RequestClientTest {

    @Autowired
    private GatewayProperties gatewayProperties;
    static WebTestClient buildSimple;

    @BeforeAll
    public static void setUp() {
        buildSimple = WebTestClient.bindToServer().baseUrl("http://172.168.1.73:9090/").responseTimeout(Duration.ofHours(1)).build();
    }
    Object lock = new Object();
    //
    @Test
    public void requestForCircuitBreakerWithoutGatewayRoute(){
        Map map = new HashMap<String,String>(){
            {
                put("flag","success");
                put("wait-time","12000");
            }
        };
        buildSimple.post().uri("/timeout").header("content-type", MediaType.APPLICATION_JSON_VALUE).body(Mono.just(map),Map.class).exchange().expectBody(String.class).consumeWith(stringEntityExchangeResult -> {
            String responseBody = stringEntityExchangeResult.getResponseBody();
            log.info(stringEntityExchangeResult.getResponseHeaders().toString());
            Assertions.assertEquals(responseBody,"data:{\"scanAvailable\":true}");
        });
    }
    @Test
    public void requestLocalServiceWithCircuitBreakerToTimeOut(){
        // data:{"scanAvailable":true} ?
        buildSimple.post().uri("/circuitBreaker/delay/2").header("Host","www.circuitbreakertimeout.org").exchange().expectBody(Map.class).consumeWith(mapEntityExchangeResult -> {
            HttpStatus status = mapEntityExchangeResult.getStatus();
            log.info("====={},{}",mapEntityExchangeResult.getResponseHeaders(),mapEntityExchangeResult.getResponseBody());
        });
    }
    @Test
    public void requestLocalServiceWithCircuitBreakerFallbackForTimeOut(){
        buildSimple.post().uri("/get/map").
                header("Host","www.circuitbreakertimeoutfallbackController.org").exchange().expectBody(Map.class).consumeWith(mapEntityExchangeResult -> {
            HttpStatus status = mapEntityExchangeResult.getStatus();
            log.info("====={},{}",mapEntityExchangeResult.getResponseHeaders(),mapEntityExchangeResult.getResponseBody());
            Assertions.assertEquals(mapEntityExchangeResult.getResponseBody().get("key1"), "val1" );
        });
    }

    @Test
    public void requestLocalServiceWithCircuitBreakerAndNofallback(){
        buildSimple.post().uri("/circuitBreaker/open/nofallback").header("Host","www.circuitbreaker.org").exchange().expectBody(Map.class).consumeWith(mapEntityExchangeResult -> {
            HttpStatus status = mapEntityExchangeResult.getStatus();
            log.info("====={},{}",mapEntityExchangeResult.getResponseHeaders(),mapEntityExchangeResult.getResponseBody());
            Assertions.assertTrue(status==SERVICE_UNAVAILABLE);
        });
    }

    @Test
    public void requestLocalService(){
        buildSimple.post().uri("/rewrite").exchange().expectBody(Map.class).consumeWith(mapEntityExchangeResult -> {
            Map responseBody = mapEntityExchangeResult.getResponseBody();
            HttpHeaders responseHeaders = mapEntityExchangeResult.getResponseHeaders();
            assertThat(responseHeaders).containsEntry("X-Response-Default-Foo", Lists.newArrayList("Default-Bar"));
            assertThat(responseBody).containsEntry("type","rewrite");
        });
        buildSimple.get().uri("/get").exchange().expectBody(String.class).consumeWith(mapEntityExchangeResult -> {
            String responseBody = mapEntityExchangeResult.getResponseBody();
            assertThat(responseBody).isEqualTo("hcj");
        });
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
    @Disabled(value = "这是在主机名解析时通常出现的暂时错误，它意味着本地服务器没有从权威服务器上收到响应" +
            "could local client dont get response from the office server")
    @Test
    public void get4(){
        buildSimple.post().uri("/post").header("Host","www.myhost.org").exchange().expectBody(Map.class).consumeWith((EntityExchangeResult<Map> result)->{
            Map responseBody = result.getResponseBody();
            Assertions.assertEquals(responseBody.get("url"),"http://www.myhost.org/post");
            log.info("host:{}",responseBody);
        });
    }
    @Disabled(value = "这是在主机名解析时通常出现的暂时错误，它意味着本地服务器没有从权威服务器上收到响应" +
            "could local client dont get response from the office server")
    @Test
    public void rewrite(){
        buildSimple.post().uri("/foo/post").header("Host","www.rewrite.org").exchange().expectBody(Map.class).consumeWith((EntityExchangeResult<Map> result)->{
            Map responseBody  = result.getResponseBody();
            Assertions.assertEquals(responseBody.get("url"),"http://www.rewrite.org/post");
            log.info("rewrite:{}",responseBody);
        });
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

}
