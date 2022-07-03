package spring;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Applications;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.lang.management.ManagementFactory;

@NoArgsConstructor
class CustomApplicationContextInitializerC implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(org.springframework.context.ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(environment,
                "server.port=8761",
                "spring.application.name=eureka1",
                "eureka.client.service-url.defaultZone=http://localhost:8761/eureka",
                "eureka.instance.non-secure-port=8081",
                "eureka.instance.metadataMap.zone = zone1",
                "eureka.client.preferSameZoneEureka = true");
    }
}
@Slf4j
@SpringBootTest(classes = {SpringDemoForServer.Dummy.class},webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(initializers = {CustomApplicationContextInitializerC.class})
public class SpringDemoForServer {
    @Autowired
    private ConfigurableApplicationContext context;
    @LocalServerPort
            public int port;
    Object lock = new Object();
    @EnableAutoConfiguration
    @EnableEurekaServer
    @Configuration
    public static class Dummy{}
    @Autowired
    EurekaClient discoveryClient;
    /**

     */
    @Test
    public void demo1() throws InterruptedException {
        Applications applications = discoveryClient.getApplications();
        String name = ManagementFactory.getRuntimeMXBean().getName();
        log.info("startup web is port:{},pid:{}",port,name);
        synchronized (lock){lock.wait();}
    }



}
