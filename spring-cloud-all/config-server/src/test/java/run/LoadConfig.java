package run;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = LoadConfig.Dummy2.class)
public class LoadConfig {
    @Autowired
    ApplicationContext context;
    @Autowired
    StandardEnvironment environment;
    @Test
    public void load(){
        Utils.print(context);
        MutablePropertySources propertySources = environment.getPropertySources();
        List<String> names = names(propertySources);
        names.stream().forEach(System.out::println);
        String property = environment.getProperty("spring.profiles.active");
        Assertions.assertEquals(property,"jdbc");;

    }

    private List<String> names(MutablePropertySources propertySources) {
        List<String> list = new ArrayList<>();
        for (PropertySource<?> p : propertySources) {
            list.add(p.getName());
        }
        return list;
    }

    @ImportAutoConfiguration(value = {ConfigFileApplicationListener.class,
            PropertyPlaceholderAutoConfiguration.class})
    @Configuration
    public static class Dummy2{

    }
}
