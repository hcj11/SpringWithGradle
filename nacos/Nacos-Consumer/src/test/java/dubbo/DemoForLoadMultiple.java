package dubbo;

import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import utils.Utils;

public class DemoForLoadMultiple {
    @PropertySource(value = "classpath:/application.properties")
    @Configuration
    @ImportAutoConfiguration(classes = DubboAutoConfiguration.class)
    public static class Dummy{
    }

    @Test
    public void try1(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Dummy.class);
        Utils.print(context);
        Assertions.assertNotNull(context.getBean("zk1", RegistryConfig.class));;

    }
}
