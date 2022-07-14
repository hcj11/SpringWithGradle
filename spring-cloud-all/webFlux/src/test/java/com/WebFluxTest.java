package com;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@Slf4j
@SpringBootTest(classes = WebFluxTest.Dummy.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,properties = {"server.port=9090"})
public class WebFluxTest {
    static WebTestClient build =null;
    @Autowired
    GenericApplicationContext applicationContext;
    @Autowired
    List<WebFilter> webFilterList;

    @BeforeAll
    public static void setUp(){
         build = WebTestClient.bindToServer().baseUrl("http://localhost:9090/").build();
    }
    @Test
    public void getWebFilterListTest(){
        webFilterList.stream().forEach(webFilter -> {
            log.info("{}",webFilter.toString());
        });

    }
    @Test
    public void get(){


        build.get().uri("/get").exchange().expectBody().consumeWith(result -> {
            HttpHeaders responseHeaders = result.getResponseHeaders();
            assertThat(responseHeaders.get("forwardId")).contains(CustomFilter.forwardId);
            String s = new String(result.getResponseBody());
            Assertions.assertEquals(s,"first flux program");
        });
    }

    @Import(WebController.class)
    @EnableAutoConfiguration
    @Configuration
    static class Dummy{
        @Bean
        public CustomFilter customFilter(){
            return  new CustomFilter();
        }
    }

    static  class CustomFilter implements WebFilter {
        static final String routeId="X-RouteId";
        static final String forwardId="X-ForwardId";

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
            exchange.getAttributes().put("routeId",routeId);
            exchange.getResponse().getHeaders().add("forwardId",forwardId);

            return chain.filter(exchange);
        }
    }
}
