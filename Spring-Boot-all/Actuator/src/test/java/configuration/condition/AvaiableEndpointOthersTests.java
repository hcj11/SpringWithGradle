package configuration.condition;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import static utils.Utils.print;


@SpringBootTest(classes = AvaiableEndpointOthersTests.Dummy.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AvaiableEndpointOthersTests {
    @Autowired
    private GenericApplicationContext applicationContext;
    @Test
    public void load(){

    }
    @Test
    public void loadActuator(){
        print(applicationContext);
        Assertions.assertTrue(applicationContext.containsBean("customEndpoint"));;
    }

    @EnableAutoConfiguration
    @Configuration
    static class Dummy{

        @Bean
        @ConditionalOnAvailableEndpoint
        public CustomEndpoint customEndpoint(){
            return new CustomEndpoint();
        }
    }

    @Data
    @Endpoint(id = "customEndpoint")
    static class CustomEndpoint{
    }


}
