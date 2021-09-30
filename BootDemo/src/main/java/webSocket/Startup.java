package webSocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
class WebController{
    @GetMapping("/")
    public String hello(){
        return "helloworld";
    }

}
@EnableWebMvc
@Configuration
@EnableAutoConfiguration
public class Startup {
    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext run = (AnnotationConfigServletWebServerApplicationContext)SpringApplication.
                run(new Class[]{Startup.class,WebController.class,WebSocketConfig.class},new String[]{});
    }
}
