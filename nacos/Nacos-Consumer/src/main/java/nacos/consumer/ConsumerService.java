package nacos.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.ServletContext;
@Slf4j
@EnableDiscoveryClient
@EnableWebMvc
@SpringBootApplication(scanBasePackages = {"nacos.consumer.*"})
public class ConsumerService {
    /**
     * ≤‚ ‘—≠ª∑“¿¿µŒ Ã‚ £¨
     *
     */

    @Autowired
    ServletContext servletContext;

    public static void main(String[] args) {
        ConfigurableApplicationContext run =
                SpringApplication.run(ConsumerService.class,
                        "--spring.main.allow-bean-definition-overriding=true",
                        "--spring.main.web-application-type=SERVLET");
    }
}
