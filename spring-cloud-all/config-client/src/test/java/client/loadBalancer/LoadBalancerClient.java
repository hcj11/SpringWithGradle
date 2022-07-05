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

    @Test
    public void loadBalancerClientTest(){
        print(context);
        String eureka = environment.getProperty("eureka");
        Assertions.assertEquals(eureka,"172.168.1.73:8761");
        // appname = peer1,peer2,peer3
        // getInstance ->gateway -> local
        String uri= "http://myservice1/get";
        String forObject = restTemplate.getForObject(uri, String.class);
        Assertions.assertEquals(forObject,"hcj");

        uri= "http://peer1/get";
        String exception = restTemplate.getForObject(uri, String.class);
//
        log.info("{}",exception);
        HttpClientErrorException exception2 = restTemplate.getForObject(uri, HttpClientErrorException.class);
        // 404 : [{"timestamp":"2022-07-05T04:03:18.453+0000","status":404,"error":"Not Found","message":"No message available","path":"/get"}]
        Assertions.assertNotNull(exception2);
//        Assertions.assertEquals(exception.get);


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
