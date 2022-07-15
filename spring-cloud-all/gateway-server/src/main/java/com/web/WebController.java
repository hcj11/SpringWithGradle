package com.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("httpbin")
public class WebController {
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
