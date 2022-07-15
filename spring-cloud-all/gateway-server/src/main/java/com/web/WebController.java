package com.web;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("httpbin")
public class WebController {

    @CircuitBreaker(name = "default")
    @TimeLimiter(name = "backendA", fallbackMethod = "fallback")
    @RequestMapping(value = "timeout",method = RequestMethod.POST)
    public CompletableFuture<Mono<String>> timeout(Map<String,String> flag) throws InterruptedException {
        // mock the network request,
        if(flag.get("flag").equals("error")){
            throw new RuntimeException("intending");
        }
        return CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(12 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return Mono.just("timeout");
                }
        );
    }

    public Mono<String> fallback(Throwable throwable) {
        return Mono.just("recover from error...");
    }

    @RequestMapping(value = "/circuitbreakerfallbackController2", method = RequestMethod.POST)
    public Mono<Map<String, String>> circuitbreakerfallbackController() {
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("key", "val");
            }
        };
        return Mono.just(map);
    }

    @RequestMapping(value = "/circuitBreaker/delay/{integer}", method = RequestMethod.POST)
    public Mono<Map<String, String>> timeout(@PathVariable("integer") Integer integer) {
        log.info("wait {} seconds...", integer);
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("key", "val");
            }
        };
        return Mono.just(map).delayElement(Duration.ofSeconds(integer));
    }

    @PostMapping("/rewrite")
    public Mono<Map<String, String>> rewrite(@RequestHeader Map<String, String> headers) {
        Map<String, String> map = new HashMap<String, String>() {
            {
                this.put("type", "rewrite");
            }
        };
        return Mono.just(map);
    }

    @RequestMapping("/get/map")
    public Mono<Map<String, String>> getMap() {
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("key1", "val1");
            }
        };
        return Mono.just(map);
    }

    @RequestMapping("/get")
    public Mono<String> get(@RequestHeader Map<String, String> headers) {
        headers.entrySet().forEach(kv -> {
            String key = kv.getKey();
            String value = kv.getValue();
            log.info("key:{},val:{}", key, value);
        });
        return Mono.just("hcj");
    }
}
