package context.test;

import context.CompontScan;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.PrepareTestInstance;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.TestPropertySourceUtils;

@Slf4j
@TestPropertySource(properties = {"logging.config=classpath:/config/logback-boot.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestAnnoationWithoutDirtiesBySpringRunner.Dummy.class})
public class TestAnnoationWithoutDirtiesBySpringRunner implements InitializingBean {

    @Autowired
    private ConfigurableApplicationContext applicationContext;
    @Autowired
    private ConfigurableEnvironment environment;
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("reinitializing");
    }
    @Import(value = LoggingApplicationListener.class)
    @Configuration
    public static class Dummy{
        public Dummy(){super();}
    }
    @Test
    public void demo1(){
        CompontScan.print(applicationContext);
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(environment,"name=hcj");
    }
    @Test
    public void demo2(){
        String name = environment.getProperty("name", String.class);
        Assertions.assertNull(name);

    }
}
