package webSocketStomp;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Data
class Shout {
    private String message;
}

@Slf4j
@org.springframework.stereotype.Controller
class Controller {
    @SendTo
    @MessageMapping("/msg")
    public Shout shout(Shout shout) {
        log.info("stomp client say ,{}", shout);
        shout.setMessage("i get message");
        return shout;
    }
}
@Configuration
class CustomWebMvcConfigurer implements WebMvcConfigurer{
}
@Configuration
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer{


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue", "/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
