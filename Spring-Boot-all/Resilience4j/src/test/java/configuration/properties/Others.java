package configuration.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sound.sampled.Line;
import java.time.Duration;
import java.util.Map;

@Slf4j
@SpringBootTest(classes = Others.Dummy.class)
public class Others {
    @Autowired
    private Info info;
    @Test
    public void load(){
        // io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigurationProperties$InstanceProperties
//        io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigurationProperties$InstanceProperties.class
        log.info("{}",info);
        String val = info.getMap().get("key");
        Assertions.assertTrue(info.getTime().equals(Duration.ofHours(1)));;
        Duration parse = Duration.parse("PT1H");
        Assertions.assertEquals(val,"val" );;
        // Caused by: org.springframework.core.convert.ConverterNotFoundException: No converter found capable of converting from type [java.lang.String] to type [java.lang.Class<java.lang.Throwable>[]]
    }

    @EnableAutoConfiguration
    @Configuration
    @EnableConfigurationProperties
    static class Dummy{

        @ConfigurationProperties(prefix = "info")
        @Bean
        public Info info(){
            return new Info();
        }
    }
    @Data
    static class Info{
        private String name;
        private Duration time;
        private Map<String,String> map ;
        private Class<? extends Throwable>[] recordExceptions;

    }
}
