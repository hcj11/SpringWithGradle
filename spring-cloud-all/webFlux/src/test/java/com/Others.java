package com;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Others {

    @Test
    public void test(){
        Flux<Long> longFlux = Flux.interval(Duration.ofMillis(500)).take(2).log().onBackpressureBuffer(2);

        StepVerifier.create(longFlux).expectNext(0L,1L).verifyComplete();

    }

}
