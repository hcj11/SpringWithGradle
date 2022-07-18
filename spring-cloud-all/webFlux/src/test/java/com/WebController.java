package com;

import com.domain.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
@Slf4j
@RestController
public class WebController {

    @RequestMapping(value = "/get",produces = {"text/plain", "application/*"})
    public Mono<String> get(){
        return Mono.just("first flux program");
    }

    @RequestMapping(value = "/getReturnFluxType",produces = {"text/plain", "application/*"})
    public Flux<String> getReturnFluxType(){
        return Flux.just("first flux program","last flux program");
    }
    Flux<Long> interval = Flux.interval(Duration.ofMillis(500)).take(50).onBackpressureBuffer(50);

    @RequestMapping(value = "/getReturnJsonTypeWithIntervalTime",produces = {MediaType.APPLICATION_JSON_VALUE})
    public Flux<String> get5ReturnJsonType(){
        return interval.map(i->{return String.format("{\"age\":%s}",i );});
    }

    @RequestMapping(value = "/getReturnServerResponseType")
    public Mono<ServerResponse> getReturnServerResponseType(){
        Mono<ServerResponse> okok = ServerResponse.ok().body(Flux.just("okok"), String.class);
        return okok;
    }

    @RequestMapping("/get2")
    public CompletableFuture<Mono<String>> get2(){
        return CompletableFuture.completedFuture(Mono.just("just so" ));
    }
    @RequestMapping("/get3")
    public CompletableFuture<String> get3(){
        return CompletableFuture.completedFuture("just so" );
    }

    @RequestMapping("/localForward")
    public Mono<ServerResponse> localForward(){
        ParameterizedTypeReference<List> objectParameterizedTypeReference = new ParameterizedTypeReference<List>(){};
        Mono<List> listMono = WebFluxTest.anthorWebClient.get().uri("/get").retrieve().bodyToMono(List.class);
        return ServerResponse.ok().body(listMono,objectParameterizedTypeReference);
    }

    @RequestMapping("/forward")
    public Flux<Person> forward(){
        Flux<Person> personFlux = WebFluxTest.remoteWebClient.get().uri("/get/list/interval").accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToFlux(Person.class);
        return personFlux;

    }
}
