package com;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Others {
    @Test
    public void eee() {
        Flux<String> okok1 = Flux.just("okok");
        Mono<ServerResponse> okok = ServerResponse.ok().body(okok1, String.class);
        StepVerifier.create(okok).expectNextMatches(serverResponse ->
        {
            return serverResponse.statusCode().equals(HttpStatus.OK);
        }).verifyComplete();
    }

    @Test
    public void ddd() {
        String format = String.format("{\"age\":%s}", 1);
        Assertions.assertEquals(format, "{\"age\":1}");
    }

    @Test
    public void test() {
        Flux<Long> longFlux = Flux.interval(Duration.ofMillis(500)).take(2).log().onBackpressureBuffer(2);

        StepVerifier.create(longFlux).expectNext(0L, 1L).verifyComplete();

    }

}
