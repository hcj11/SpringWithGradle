package com;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import utils.Utils;

@SpringBootApplication
public class SpringBootGatewayApps {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("path_route", r -> r.host("*.readbody.org").filters(f->{return f.prefixPath("/")
                        .addResponseHeader("X-Default-header","X-Default-header-Val");})
                        .uri("http://httpbin.org"))
                .route("host_route", r -> r.host("*.myhost.org")
                        .uri("http://httpbin.org"))
                .route("rewrite_route", r -> r.host("*.rewrite.org")
                        .filters(f -> f.rewritePath("/foo/(?<segment>.*)", "/${segment}"))
                        .uri("http://httpbin.org"))
                .build();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SpringBootGatewayApps.class);
        Utils.print(run);
    }
}
