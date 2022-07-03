package run.webApps;

import com.web.WebController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import utils.Utils;

@SpringBootTest(classes = SpringBootGatewayApps.Dummy.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=9090"})
public class SpringBootGatewayApps implements InitializingBean {

@Autowired
    GenericApplicationContext genericApplicationContext;
    Object lock = new Object();

    @Test
    public void startUp() throws InterruptedException {
        Utils.print(genericApplicationContext);

        synchronized (lock) {
            lock.wait();
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Import(WebController.class)
    @Configuration
    @EnableAutoConfiguration
    static class Dummy {
        @Bean
        public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
            return builder.routes()
                    .route("path_route", r -> r.path("/get")
                            .uri("http://httpbin.org"))
                    .route("host_route", r -> r.host("*.myhost.org")
                            .uri("http://httpbin.org"))
                    .route("rewrite_route", r -> r.host("*.rewrite.org")
                            .filters(f -> f.rewritePath("/foo/(?<segment>.*)", "/${segment}"))
                            .uri("http://httpbin.org"))
                    .build();
        }
    }
}
