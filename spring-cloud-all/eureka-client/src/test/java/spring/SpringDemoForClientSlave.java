package spring;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Applications;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ZonePreferenceServiceInstanceListSupplier;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest(classes = {SpringDemoForClientSlave.Dummy.class},
        webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringDemoForClientSlave {
    @Autowired
    private ConfigurableApplicationContext context;
    @LocalServerPort
    public int port;

    @Autowired
    EurekaClient eurekaClient;
    @Autowired
    SimpleDiscoveryClient simpleDiscoveryClient;
    @Autowired
    DiscoveryClient discoveryClient;


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
        List<ServiceInstance> myservice1 = simpleDiscoveryClient.getInstances("myservice1");
        Assertions.assertEquals(myservice1.get(0).getServiceId(),"myservice1");
        Assertions.assertEquals(myservice1.get(0).getHost(),"localhost");

        Applications applications = eurekaClient.getApplications();

        List<InstanceInfo> peer2 = eurekaClient.getInstancesByVipAddress("app1", false);

        Assertions.assertTrue(discoveryClient instanceof CompositeDiscoveryClient);;

        List<DiscoveryClient> discoveryClients = ((CompositeDiscoveryClient) discoveryClient).getDiscoveryClients();

        List<ServiceInstance> peer21 = discoveryClient.getInstances("app1");
        Assertions.assertEquals(peer2.get(0).getId(),peer21.get(0).getServiceId());

        synchronized (lock){lock.wait();}
    }



}
