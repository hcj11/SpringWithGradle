package context.test;

import context.CompontScan;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.support.TestPropertySourceUtils;

@Slf4j
@SpringBootTest(classes = {TestAnnoationWithoutDirtiesBySpringBootTestContextBootstrapper.Dummy.class})
public class TestAnnoationWithoutDirtiesBySpringBootTestContextBootstrapper implements InitializingBean {

    @Autowired
    private ConfigurableApplicationContext applicationContext;
    @Autowired
    private ConfigurableEnvironment environment;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("reinitializing");
    }

    @Import(LoggingApplicationListener.class)
    @Configuration
    public static class Dummy {
        public Dummy() {
            super();
        }
    }

    @Test
    public void demo1() {
        CompontScan.print(applicationContext);
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(environment, "name=hcj");
    }

    @Test
    public void demo2() {
        String name = environment.getProperty("name", String.class);
        Assertions.assertNotNull(name);

    }
}
