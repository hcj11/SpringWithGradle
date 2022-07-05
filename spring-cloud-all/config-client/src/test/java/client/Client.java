package client;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.ArrayList;
import java.util.List;

import static utils.Utils.print;

@SpringBootTest(classes = Client.Dummy.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Client {
    @LocalServerPort
    private int port;
    @Autowired
    GenericApplicationContext context;;
    @Autowired
    StandardEnvironment environment;



    private List<String> names(MutablePropertySources propertySources) {

        List<String> list = new ArrayList<>();
        for (PropertySource<?> p : propertySources) {
            list.add(p.getName());
        }
        return list;
    }
    @Test
    public void enableProperties(){
        Info bean = context.getBean(Info.class);
        Assertions.assertEquals(bean.getMsg(),"hcjhcj");
    }


    @Test
    public void loadConfigFromServer(){
        print(context);
        String property1 = environment.getProperty("spring.application.name");
        Assertions.assertEquals(property1,"config-client");

        String property = environment.getProperty("a.b.c");
        String property2 = environment.getProperty("a.c");
        names(environment.getPropertySources()).stream().forEach(System.out::println);

        Assertions.assertEquals(property,"x" );
        Assertions.assertEquals(property2,"ghj" );
        String peer2 = environment.getProperty("peer2");

        Assertions.assertEquals(peer2,"localhost");



    }

    @EnableConfigurationProperties
    @EnableAutoConfiguration
    @Configuration
    static class Dummy{
        @Bean
        public Info info(){
            return new Info();
        }
    }

    @Setter
    @Getter
    @ConfigurationProperties(prefix = "info")
    static class Info{
        private String msg;
    }
}
