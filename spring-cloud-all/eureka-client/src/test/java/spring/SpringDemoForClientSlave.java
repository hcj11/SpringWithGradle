package spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = {SpringDemoForClientSlave.Dummy.class},
        webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {})
public class SpringDemoForClientSlave {
    @Autowired
    private ConfigurableApplicationContext context;
    @LocalServerPort
    public int port;

    Object lock = new Object();

    @EnableEurekaClient
    @EnableAutoConfiguration
    @Configuration
    public static class Dummy{}
    /**
     *
     */
    @Test
    public void demo1() throws InterruptedException {

        synchronized (lock){lock.wait();}
    }



}
