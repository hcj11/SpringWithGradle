package com.functions;

import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandRequest;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.util.RouteMatcher;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

public class Routefunction {
    @Test
    public void load(){

//        RouterFunction<ServerResponse> route = RouterFunctions.route().GET("/get",
//                request->ServerResponse.ok().body(Mono.just("hcj"),String.class)).build();
//
//        MockServerHttpRequest request = MockServerHttpRequest.get("http://example.org").build();
//        new DefaultServerRequest(MockServerWebExchange.from(request), Collections.emptyList());

    }
}
