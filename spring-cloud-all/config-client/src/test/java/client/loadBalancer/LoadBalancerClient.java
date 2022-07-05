package client.loadBalancer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static utils.Utils.names;
import static utils.Utils.print;
@Slf4j
@SpringBootTest(classes = LoadBalancerClient.Dummy.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoadBalancerClient {
    @LocalServerPort
    private int port;
    @Autowired
    GenericApplicationContext context;;
    @Autowired
    StandardEnvironment environment;
    @Autowired
    RestTemplate restTemplate;
//    @Test
    //   LoadBalancerClientFactory loadBalancerClientFactory = new LoadBalancerClientFactory();
    //        loadBalancerClientFactory.getInstance("",ReactorServiceInstanceLoadBalancer.class);
    @Test
    public void loadBalancerClientTest(){
        print(context);
        String eureka = environment.getProperty("eureka");
        Assertions.assertEquals(eureka,"172.168.1.73:8761");
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);


        String uri= "http://myservice1/get";
        String forObject = restTemplate.getForObject(uri, String.class);
        Assertions.assertEquals(forObject,"hcj");

        uri= "http://peer2/actuator/env";
        String env = restTemplate.getForObject(uri, String.class);
        log.info("{}",env);


    }

    @Test
    public void loadConfigFromServer(){
        print(context);
        names(environment.getPropertySources()).stream().forEach(System.out::println);

    }

    @EnableConfigurationProperties
    @EnableAutoConfiguration
    @Configuration
    static class Dummy{

        @LoadBalanced
        @Bean
        public RestTemplate restTemplate(){
            return new RestTemplate();
        }
    }
}
