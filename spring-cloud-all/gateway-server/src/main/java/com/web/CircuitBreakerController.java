package com.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("circuitBreaker")
public class CircuitBreakerController {

    @RequestMapping(value = "/delay/{integer}")
    public void timeout(@PathVariable("integer") Integer integer){
        log.info("wait {} seconds...",integer);
        // gateway
        Mono.delay(Duration.ofSeconds(integer));
    }
    @RequestMapping(value = "/open")
    public void circuitBreakerOpen(){
        log.info("circuitBreakerOpen...");
    }
}
