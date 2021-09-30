package webSocketStomp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@EnableWebSocketMessageBroker
@Configuration
@EnableAutoConfiguration
public class Startup {
    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext run =
                (AnnotationConfigServletWebServerApplicationContext) SpringApplication.
                run(new Class[]{Startup.class,Controller.class, WebSocketStompConfig.class,
                        CustomWebMvcConfigurer.class},new String[]{});

    }
}