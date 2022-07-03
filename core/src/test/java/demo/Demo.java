package demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Demo.Dummy.class})
public class Demo {

    @Autowired
    private ConfigurableApplicationContext context;
    @Autowired
    private Environment environment;

    @Configuration
    public static class Dummy {

    }

    @Test
    public void try1() {
        // spring.profiles.active=peer3
//        String[] activeProfiles = environment.getActiveProfiles();
//        log.info("{}",activeProfiles[0]);
        String computername = environment.getProperty("COMPUTERNAME");
        Assertions.assertEquals(computername,"0JRXX56OU4ME1C4");
//        String foobar = System.getProperty("foo");
//        Assertions.assertEquals(foobar,"bar");
        String bar = environment.getProperty("foo");
        Assertions.assertEquals(bar,"bar");
        String property = environment.getProperty("spring.profiles.active");
        Assertions.assertEquals(property, "peer31");

    }
}
