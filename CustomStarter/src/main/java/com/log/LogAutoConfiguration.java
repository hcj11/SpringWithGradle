package com.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.logging.logback.LogbackLoggingSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Data
class CustomA{
    private String name;
}
//@ConditionalOnExpression("#{name=='hcj'}")
//@ConditionalOnProperty("name")
@Slf4j
@Configuration(proxyBeanMethods = false)
public class LogAutoConfiguration {

    @Bean
    public CustomA customA(){
        log.info("before");
        return new CustomA("hcj");
    }
}
