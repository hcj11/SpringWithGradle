package com;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class WebController {

    @RequestMapping("/get")
    public Mono<String> get(){
        return Mono.just("first flux program");
    }
    @RequestMapping("/get2")
    public String get2(){
        return "";
    }
}
