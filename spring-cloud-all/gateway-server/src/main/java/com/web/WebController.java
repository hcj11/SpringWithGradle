package com.web;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
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

    @Retry(name = "default")
    @RateLimiter(name = "default")
    @Bulkhead(name = "default",type = Bulkhead.Type.THREADPOOL)
    @CircuitBreaker(name = "default")
    @TimeLimiter(name = "backendA", fallbackMethod = "fallback")
    @RequestMapping(value = "timeout",method = RequestMethod.POST)
    public CompletableFuture<String> timeout(@RequestBody Map<String,String> flag) throws InterruptedException {
        // mock the network request,
        log.info("param: {}",flag);
        if(flag.get("flag").equals("error")){
            throw new RuntimeException("intending"); // throws excption as failure or success .
        }
        Integer waittime = Integer.valueOf(flag.get("wait-time"));
        return CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(waittime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "timeout";
                }
        );
    }
    public CompletableFuture<String> fallback(Map<String,String> flag,Throwable throwable) {
        if(throwable!=null){    log.error("===============msg:{}",throwable.getMessage());}
        return CompletableFuture.completedFuture("recover from error...");
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
