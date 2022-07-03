package spring;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Applications;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration;
import org.springframework.cloud.netflix.eureka.config.DiscoveryClientOptionalArgsConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import utils.Utils;

import java.util.List;

@NoArgsConstructor
class CustomApplicationContextInitializerC implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(org.springframework.context.ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(environment,"eureka.client.enabled=true");
    }
}

@SpringBootTest(classes = {SpringDemoForClient.Dummy.class},
        webEnvironment= SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(initializers = {CustomApplicationContextInitializerC.class})
public class SpringDemoForClient {
    @Autowired
    private ConfigurableApplicationContext context;
    Object lock = new Object();
    @Autowired(required = false)
    private EurekaClient eurekaClient;


    @EnableEurekaClient
//    @ImportAutoConfiguration({DiscoveryClientOptionalArgsConfiguration.class,EurekaClientAutoConfiguration.class,EurekaDiscoveryClientConfiguration.class})
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
