package com;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.stream.Stream;
/**
 * 测试SpringApplication load step
 * 1. autoconfiguration scan bean under the configuration
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "custom")
public class Config {
    @Autowired
    ApplicationContext applicationContext;


    public static void print(ListableBeanFactory context) {

        Stream.of(context.getBeanDefinitionNames()).forEach(System.out::println);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Config.class);
        log.info("=================startup");
        print(run.getBeanFactory());
    }

}
