package com;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.util.RouteMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;
import utils.Utils;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class SpringBootGatewayApps {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

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
    @Bean
    public RouterFunction<ServerResponse> routerFunction(){
        // HandlerFunction<T> handlerFunction todo test the uri . from webflux
        return route(POST("/circuitbreakertimeoutException"),handlerFunction());
    }

    public HandlerFunction<ServerResponse> handlerFunction(){
        return (ServerRequest  request)->{
            Throwable throwable = (Throwable) request.attribute(CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR).orElse(null);
            Mono<ServerResponse> build =null;
            if(throwable!=null){
                build =  ServerResponse.ok().header("exception",throwable.getMessage()).build();
            }else{
                build = ServerResponse.ok().header("exception", "nothing").build();
            }
            return build;
        } ;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SpringBootGatewayApps.class);
        Utils.print(run);
    }
}
