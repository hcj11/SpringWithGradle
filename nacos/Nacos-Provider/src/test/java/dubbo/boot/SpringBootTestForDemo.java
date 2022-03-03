package dubbo.boot;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.RegistryConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.mock.env.MockPropertySource;
import org.springframework.test.context.ContextConfiguration;
import utils.Utils;

@NoArgsConstructor
class CustomApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(org.springframework.context.ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Utils.reference.set(environment);
        applicationContext.setEnvironment(new SpringBootTestForDemo.CustomStandardEnvironment());
    }
}


/**
 * start the service
 */
@SpringBootTest
@Slf4j
@ContextConfiguration(initializers = {CustomApplicationContextInitializer.class})
public class SpringBootTestForDemo {
    @Autowired
    private AnnotationConfigApplicationContext applicationContext;

    @SpringBootApplication
    public static class Dummy {
    }
    @NoArgsConstructor
   public static class CustomStandardEnvironment extends StandardEnvironment {

        @Override
        protected void customizePropertySources(MutablePropertySources propertySources) {
            ConfigurableEnvironment configurableEnvironment = Utils.reference.get();

            MutablePropertySources propertySources1 = configurableEnvironment.getPropertySources();
            propertySources1.forEach(propertySource -> {
                propertySources.addLast(propertySource);
            });
            /**
             * and start server  this .
             */
            PropertySource<?> source = new MockPropertySource().withProperty("dubbo.zk.port", String.valueOf(3222));
            propertySources.addLast(source);
            super.customizePropertySources(propertySources);
        }
    }
    @BeforeEach
    public void setUp() throws Exception {

    }

    @Test
    public void try1() {
        Utils.print(applicationContext);
        /**
         * 构建 context,
         */
        RegistryConfig zk1 = applicationContext.getBean("zk1", RegistryConfig.class);
        Assertions.assertEquals(zk1.getAddress(), "zookeeper://localhost:3222");
    }
}
