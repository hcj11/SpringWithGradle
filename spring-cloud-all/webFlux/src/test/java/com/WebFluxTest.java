package com;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(classes = WebFluxTest.Dummy.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,properties = {"server.port=9090"})
public class WebFluxTest {
    static WebTestClient build =null;
    @BeforeAll
    public static void setUp(){
         build = WebTestClient.bindToServer().baseUrl("http://localhost:9090/").build();
    }

    @Test
    public void get(){
        build.get().uri("/get").exchange().expectBody().consumeWith(result -> {
            String s = new String(result.getResponseBody());
            Assertions.assertEquals(s,"first flux program");
        });
    }

    @Import(WebController.class)
    @EnableAutoConfiguration
    @Configuration
    static class Dummy{}
}
