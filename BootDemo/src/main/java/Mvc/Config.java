package Mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.io.InputStream;

@PropertySource(value = {"classpath:/auth/auth.properties",
        "classpath:/password/password.properties"},ignoreResourceNotFound = true)
@Configuration
@Slf4j
@EnableWebMvc
@EnableAutoConfiguration
@ComponentScan(
        excludeFilters = { @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public class Config {

    public static void main(String[] args) throws IOException {
        /**
         *   SpringApplication.run(com.paic.Config.class, args)
         */
        ConfigurableApplicationContext run = SpringApplication.run(Config.class, args);
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        InputStream resourceAsStream = contextClassLoader.getResourceAsStream("Mvc/others.txt");
        log.info("{}",resourceAsStream!=null);
        log.info("SingletonCount : {}",run.getBeanFactory().getSingletonCount());;
        /**
         */
        Environment bean = run.getBean(Environment.class);
        System.out.println(bean);
        String hello = bean.getProperty("hello");
        String password = bean.getProperty("password");
        log.info("{},{}",hello,password);

    }


}
