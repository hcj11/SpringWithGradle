package dubbo.boot;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.test.TestingServer;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.config.ProtocolConfig;
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
class CustomApplicationContextInitializerC implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(org.springframework.context.ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Utils.reference.set(environment);
        applicationContext.setEnvironment(new SpringBootTestForDubboSlave.CustomStandardEnvironment());
    }
}
/**
 start the service
 */
@SpringBootTest(classes = {SpringBootTestForDubbo.Dummy.class})
@Slf4j
@ContextConfiguration(initializers = {CustomApplicationContextInitializerC.class})
public class SpringBootTestForDubboSlave {
    @Autowired
    private AnnotationConfigApplicationContext applicationContext;

    @SpringBootApplication
    public static class Dummy{
    }
    public static TestingServer zkServer;

    public static class CustomStandardEnvironment extends StandardEnvironment{
        @Override
        protected void customizePropertySources(MutablePropertySources propertySources) {
            ConfigurableEnvironment configurableEnvironment = Utils.reference.get();

            MutablePropertySources propertySources1 = configurableEnvironment.getPropertySources();
            propertySources1.forEach(propertySource -> {
                propertySources.addLast(propertySource);
            });
            int zkServerPort = 2181 ;
            System.out.println("bind port is "+zkServerPort);
            PropertySource<?> source = new MockPropertySource().withProperty("dubbo.zk.port", String.valueOf(zkServerPort))
                    .withProperty("dubbo.dubbo1.port",Utils.serverPort.incrementAndGet() + 2 ); //12348
            propertySources.addLast(source);
            super.customizePropertySources(propertySources);
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
    }

    Object lock = new Object();
    @Test
    public void startUp() throws InterruptedException {
        /**
         start the consumer .
         */
        Utils.print(applicationContext);
        synchronized (lock){lock.wait();}

    }

}
