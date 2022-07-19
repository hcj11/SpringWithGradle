package run;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest(classes = {Others.Dummy.class})
public class Others {
    @Autowired
    ObjectS objectS;
    @Test
    public void verfiy(){
        System.out.println(objectS);
    }

    @Configuration
    @EnableAutoConfiguration
    @EnableConfigurationProperties
    static class Dummy{
        @Bean
        public ObjectS objectS(){
            return new ObjectS();
        }
    }
    @Setter
    @ConfigurationProperties(prefix = "obj")
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class ObjectS{
        private int age;
        private String name;
    }
}
