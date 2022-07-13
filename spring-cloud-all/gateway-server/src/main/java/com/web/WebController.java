package com.web;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("httpbin")
public class WebController {

    @PostMapping("circuitbreaker")
    public Mono<String> circuitbreaker(){
        return Mono.just("circuitbreaker");
    }

    @PostMapping("/rewrite")
    public Mono<Map<String,String>> rewrite(@RequestHeader Map<String,String> headers){
        Map<String,String> map = new HashMap<String,String>(){
            {
                this.put("type","rewrite");
            }
        };
        return Mono.just(map);
    }

    @RequestMapping("/get")
    public Mono<String> get(@RequestHeader Map<String,String> headers){
        headers.entrySet().forEach(kv->{
            String key = kv.getKey();
            String value = kv.getValue();
            log.info("key:{},val:{}",key,value);
        });
        return Mono.just("hcj");
    }
}
