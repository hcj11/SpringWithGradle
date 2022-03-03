package dubbo.boot;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.test.TestingServer;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.registry.zookeeper.ZookeeperRegistry;
import org.apache.dubbo.registry.zookeeper.ZookeeperRegistryFactory;
import org.apache.dubbo.remoting.zookeeper.curator.CuratorZookeeperTransporter;
import org.apache.dubbo.rpc.Protocol;
import org.apache.dubbo.rpc.protocol.dubbo.DubboProtocol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.*;
import org.springframework.mock.env.MockPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import utils.Utils;

import static org.springframework.core.env.StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME;
@NoArgsConstructor
class CustomApplicationContextInitializerB implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(org.springframework.context.ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Utils.reference.set(environment);
        applicationContext.setEnvironment(new SpringBootTestForDubbo.CustomStandardEnvironment());
    }
}

/**
 start the service
 */
@SpringBootTest(classes = {SpringBootTestForDubbo.Dummy.class})
@Slf4j
@ContextConfiguration(initializers = {CustomApplicationContextInitializerB.class})
public class SpringBootTestForDubbo {
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
            /**
             * and start server  this .
             */
//            int zkServerPort = NetUtils.getAvailablePort();
            int zkServerPort = 2181;
            try {
                zkServer = new TestingServer(zkServerPort, true);
                zkServer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("bind port is "+zkServerPort);

            org.springframework.core.env.PropertySource<?> source = new MockPropertySource().withProperty("dubbo.zk.port", String.valueOf(zkServerPort))
                    .withProperty("dubbo.dubbo1.port",Utils.serverPort.incrementAndGet());;
            propertySources.addLast(source);
            super.customizePropertySources(propertySources);
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void multipleConfigTest() throws InterruptedException {
        ProtocolConfig dubbo1 = applicationContext.getBean("dubbo1", ProtocolConfig.class);
        RegistryConfig zk1 = applicationContext.getBean("zk1", RegistryConfig.class);
        Assertions.assertNotNull(dubbo1);
        Assertions.assertNotNull(zk1);
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
