package resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JAutoConfiguration;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import utils.Utils;

@SpringBootTest(classes = Resilience4jConfig.Dummy.class)
public class Resilience4jConfig {
    @Autowired
    GenericApplicationContext applicationContext;
    @Autowired
    CircuitBreakerRegistry circuitBreakerRegistry;
    @Autowired
    Resilience4JCircuitBreakerFactory resilience4JCircuitBreakerFactory;


    @Test
    public void load(){
        Utils.print(applicationContext);
        // Resilience4JCircuitBreakerFactory // ensure  the config .
//        Resilience4JAutoConfiguration.class;


    }

    @EnableAutoConfiguration
    @Configuration
    static class Dummy{}
}
