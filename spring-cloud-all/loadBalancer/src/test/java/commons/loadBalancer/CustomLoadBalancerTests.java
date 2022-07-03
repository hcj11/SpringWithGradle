package commons.loadBalancer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = CustomLoadBalancerTests.Dummy.class,
        properties = {
        "spring.cloud.discovery.client.simple.instances.myservice1[0].serviceId=service1",
        "spring.cloud.discovery.client.simple.instances.myservice1[0].host=discovery-client",
        "spring.cloud.discovery.client.simple.instances.myservice1[0].port=8080",
        "spring.cloud.discovery.client.simple.instances.myservice1[0].secure=false",
        "spring.cloud.discovery.client.simple.instances.myservice1[1].serviceId=service2",
        "spring.cloud.discovery.client.simple.instances.myservice1[1].host=localhost",
        "spring.cloud.discovery.client.simple.instances.myservice1[1].port=8081",
        "spring.cloud.discovery.client.simple.instances.myservice1[1].secure=false"
})
public class CustomLoadBalancerTests {


    @LoadBalanced
    public RestTemplate restTemplate;

    @Autowired
    private BlockingLoadBalancerClient loadBalancerClient;
    @Autowired
    SimpleDiscoveryProperties simpleDiscoveryProperties;
    @Autowired
    SimpleDiscoveryClient simpleDiscoveryClient;
    @Autowired
    DiscoveryClient discoveryClient;

    @Test
    public void test(){
        Assertions.assertTrue(discoveryClient instanceof CompositeDiscoveryClient);;

        List<ServiceInstance> myservice1 = simpleDiscoveryClient.getInstances("myservice1");
        Assertions.assertEquals(myservice1.size(),2);
        assertThat(myservice1.get(0).getServiceId()).isIn("myservice1");

    }

    @EnableAutoConfiguration
    @Configuration
    static class Dummy {
    }
}
