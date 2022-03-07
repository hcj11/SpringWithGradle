package spring;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.eureka.EurekaServerConfig;
import com.netflix.eureka.cluster.PeerEurekaNodes;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.CloudEurekaClient;
import org.springframework.cloud.netflix.eureka.http.EurekaApplications;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.eureka.server.EurekaServerConfigBean;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.lang.management.ManagementFactory;
import java.util.List;


@Slf4j
@TestPropertySource(properties = {"debug=true"})
@SpringBootTest(classes = {SpringDemoForServer2.Dummy.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
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
                    "eureka.server.remote-region-urls=http://localhost:8761",
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
    @Import(WebController.class)
    public static class Dummy {
    }

    /**
     *
     */
    @Test
    public void demo1() throws InterruptedException {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        log.info("startup web is port:{},pid:{}", port, name); //54436

        synchronized (lock) {
            lock.wait();
        }
    }

    @RestController
    @Configuration
    @Data
    static class WebController {

        @Autowired
        DiscoveryClient discoveryClient;


        @GetMapping(value = "get")
        public void getInfo(){
            // Assert the serviceId = eureka1 ,eureka2
            List<String> services = discoveryClient.getServices();
            List<ServiceInstance> instances = discoveryClient.getInstances(services.get(0));
            CloudEurekaClient discoveryClient = (CloudEurekaClient) this.discoveryClient;
            discoveryClient.getApplications();


            EurekaServerConfigBean eurekaServerConfigBean = new EurekaServerConfigBean();


        }
    }


}
