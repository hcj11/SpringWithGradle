package com.paic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.boot.starter.autoconfigure.SwaggerUiWebMvcConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.configuration.Swagger2WebMvcConfiguration;

import javax.servlet.ServletContext;

//        ConfigClientAutoConfiguration.class
//        ConfigServiceBootstrapConfiguration.class
@Slf4j
@EnableWebMvc
@EnableSwagger2
@SpringBootApplication(scanBasePackages = "com.paic.*")
public class StartUp {

//    static {
//        System.setProperty(STANDALONE_MODE_PROPERTY_NAME, "true");
//    }
    @Autowired
    ServletContext servletContext;


    public static void main(String[] args) {
//        SwaggerAutoConfiguration.class;
//        SwaggerUiWebMvcConfiguration.class;
//        Swagger2WebMvcConfiguration.class;

// localhost:8848
        ConfigurableApplicationContext run =
                SpringApplication.run(StartUp.class,
                        "--spring.main.allow-bean-definition-overriding=true",
                        "--spring.main.web-application-type=SERVLET");
    }
}
