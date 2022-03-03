package boot;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.*;
import org.springframework.mock.env.MockPropertySource;

import static org.springframework.core.env.StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME;

@Slf4j
@SpringBootTest
public class TransfromInSpringBoot implements EnvironmentAware , BeanFactoryAware {
    @Autowired
    private AnnotationConfigApplicationContext applicationContext;
     int port = 0;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
//        beanFactory.getBean()
//        StandardEnvironment standard = (StandardEnvironment) environment;
//        PropertySource<?> source = new MockPropertySource().withProperty("zk_port", String.valueOf(port));
//        propertySources.addLast(source);
    }

    @Configuration
    @EnableAutoConfiguration
    public static class Dummy{}
    /**
     * and the order of running  ,
     */
    @BeforeEach
    public void setUp() {
        log.info("i am here!!!, to setUp");
        this.port = 2182;
    }
    static {
        System.setProperty("hello","world");
    }

    @Override
    public void setEnvironment(Environment environment) {

        StandardEnvironment standard = (StandardEnvironment) environment;
        standard.getPropertySources().forEach(propertySource -> {
            log.info("=={}", propertySource.toString());
        });
        MutablePropertySources propertySources = standard.getPropertySources();
        MapPropertySource defaultProperties = (MapPropertySource) propertySources.get(SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME);
        Assertions.assertEquals(defaultProperties.getProperty("hello"),"world");
        PropertySource<?> source = new MockPropertySource().withProperty("zk_port", String.valueOf(2182));
        propertySources.addLast(source);

    }

    @Test
    public void try1() {
        StandardEnvironment bean = applicationContext.getBean(StandardEnvironment.class);
        Assertions.assertEquals(bean.getProperty("zk.port"), "2182");
        Assertions.assertEquals(bean.getProperty("oldReference"), "2182");
        Assertions.assertEquals(bean.getProperty("oldReferenceForZk_port"), "2182");

    }


}

