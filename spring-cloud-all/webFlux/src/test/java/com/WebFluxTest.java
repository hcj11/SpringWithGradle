package com;

import com.domain.Person;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.WebHandler;
import org.springframework.web.servlet.function.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

@Slf4j
@SpringBootTest(classes = WebFluxTest.Dummy.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,properties = {"server.port=9090"})
public class WebFluxTest {
    static WebTestClient build =null;
    static WebClient webClient =null;
    static WebClient remoteWebClient =null;
    static WebClient anthorWebClient =null;
    @Autowired
    GenericApplicationContext applicationContext;
    @Autowired
    List<WebFilter> webFilterList;

    @BeforeAll
    public static void setUp(){
          webClient = WebClient.create("http://localhost:9090");;
          anthorWebClient = WebClient.create("http://localhost:9090");;
          remoteWebClient = WebClient.create("http://172.168.1.73:9090");
          build = WebTestClient.bindToServer().baseUrl("http://localhost:9090/").build();
    }
    @Test
    public void forwardTestWithSingleMachineForward(){
        Flux<Person> flux = WebFluxTest.remoteWebClient.get().uri("/get/list/interval")
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(Person.class);
        StepVerifier.create(flux).expectNext(
                new Person(0),new Person(1),new Person(2),
                new Person(3),new Person(4),new Person(5),
                new Person(6),new Person(7),new Person(8),new Person(9)
        ).verifyComplete();
    }
    // remoteWebClient
    @Test
    public void forwardTestWithMultiMachineForward(){
        Flux<Person> flux = WebFluxTest.webClient.get().uri("/forward")
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(Person.class);
        StepVerifier.create(flux).expectNext(new Person(0),new Person(1),
                new Person(2),      new Person(3),new Person(4),new Person(5),
                new Person(6),new Person(7),new Person(8),new Person(9)
        ).verifyComplete();

    }
    @Test
    public void getWebFilterListTest(){
        webFilterList.stream().forEach(webFilter -> {
            log.info("{}",webFilter.toString());
        });
    }
    @Test
    public void getReturnServerResponseType(){
        Mono<ServerResponse> mono = WebFluxTest.webClient.get()
                .uri("/getReturnServerResponseType").retrieve()
                .bodyToMono(ServerResponse.class).log().doOnError(throwable -> {
                    throwable.getStackTrace();
                });
        StepVerifier.create(mono ).expectNextMatches(serverResponse -> {
            return serverResponse.statusCode()== HttpStatus.OK;
        }).verifyComplete();

    }
    @Test
    public void getReturnJsonTypeWithIntervalTime(){
        Flux<Person> flux = WebFluxTest.webClient.get().uri("/getReturnJsonTypeWithIntervalTime")
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(Person.class);
        StepVerifier.create(flux).expectNext(new Person(0)).expectNext(new Person(1))
                .thenCancel().verify(Duration.ofSeconds(5L * 100));
    }
    @Test
    public void get5(){
        Flux<String> flux = WebFluxTest.webClient.get().uri("/getReturnFluxType").retrieve().bodyToFlux(String.class);
        String s =  flux.blockFirst();
        assertThat(s).endsWith("first flux programlast flux program");
    }
    @Test
    public void get4(){
        Mono<String> listMono = WebFluxTest.webClient.get().uri("/get").retrieve().bodyToMono(String.class);
        String block = listMono.block();
        Assertions.assertEquals(block,"first flux program");

    }
    @Test
    public void localForwardMachine(){
        Mono<String> mono = webClient.get().uri("/localForward").retrieve().bodyToMono(String.class);
        String block = mono.block();
        Assertions.assertEquals(block, "");


    }
    @Test
    public void forwardMachine(){
       webClient.get().uri("/forward").exchange().log().block();
    }
    @Test
    public void verfiyCompletableFuture(){
        Flux<String> stringFlux = webClient.get()
                .uri("/get2")
                .accept(TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);

        StepVerifier.create(stringFlux)
                .expectNext("data:{\"scanAvailable\":true}")
                .verifyComplete();
    }
    @Test
    public void get3(){
        build.get().uri("/get3").exchange().expectBody(String.class).consumeWith(result -> {
            String responseBody = result.getResponseBody();
            Assertions.assertEquals(responseBody,"just so");
        });
    }
    @Test
    public void getAndReturnCompletableFuture(){
        build.get().uri("/get2").exchange().expectBody(String.class).consumeWith(result -> {
            String responseBody = result.getResponseBody();
            Assertions.assertEquals(responseBody,"data:{\"scanAvailable\":true}");
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
    @EnableWebFlux
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
