package spring;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.lang.management.ManagementFactory;


@Slf4j
@SpringBootTest(classes = {SpringDemoForServer.Dummy.class},webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(initializers = {SpringDemoForServer2.CustomApplicationContextInitializerC.class})
public class SpringDemoForServer2 {
    @NoArgsConstructor
    public static class CustomApplicationContextInitializerC implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(environment,
                    "server.port=8762",
                    "eureka.client.service-url.defaultZone=http://localhost:8762/eureka",
                    "spring.application.name=eureka2",
                    "eureka.instance.metadataMap.zone = zone2",
                    "eureka.client.preferSameZoneEureka = true");
        }
    }

    @Autowired
    private ConfigurableApplicationContext context;
    @LocalServerPort
            public int port;
    Object lock = new Object();
    @EnableAutoConfiguration
    @EnableEurekaServer
    @Configuration
    public static class Dummy{}
    /**
     *
     */
    @Test
    public void demo1() throws InterruptedException {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        log.info("startup web is port:{},pid:{}",port,name);

        synchronized (lock){lock.wait();}
    }



}
