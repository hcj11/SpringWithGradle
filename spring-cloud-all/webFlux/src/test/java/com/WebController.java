package com;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
@Slf4j
@RestController
public class WebController {

    @RequestMapping("/get")
    public Mono<String> get(){
        return Mono.just("first flux program");
    }
    @RequestMapping("/get2")
    public CompletableFuture<Mono<String>> get2(){
        return CompletableFuture.completedFuture(Mono.just("just so" ));
    }
    @RequestMapping("/get3")
    public CompletableFuture<String> get3(){
        return CompletableFuture.completedFuture("just so" );
    }
    @RequestMapping("/forward")
    public void forward(){
        WebFluxTest.webClient.get().uri("/get").exchange().log().doOnNext(clientResponse -> {
            Mono<String> mono = clientResponse.bodyToMono(String.class);
            mono.doOnNext(s -> {
                log.info("{}",s);
            });
        });
    }
}
