package com;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

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
}
