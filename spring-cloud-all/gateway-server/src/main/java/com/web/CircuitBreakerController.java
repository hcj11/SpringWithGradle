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



//    @RequestMapping(value = "/open")
    public void circuitBreakerOpen(){
        log.info("circuitBreakerOpen...");
    }
}
