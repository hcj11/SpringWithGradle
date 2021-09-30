package cloud.commons;

import cn.hutool.core.lang.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * bootstrapPostpress
 *
 */
@SpringBootTest(properties = {"key=value","key1=value2"} ,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(SpringRunner.class)
public class Cloud {

    @LocalServerPort
    private int port;

    @Autowired
    private ConfigurableEnvironment environment;
    /**
     * º”‘ÿ static class°£
     */
    @Configuration
    @EnableAutoConfiguration
    public static class TestBean{
    }

    @Test
    public void run(){
        Map map1 = (Map) environment.getPropertySources().get("test-epp-map").getProperty("map1");
        Assert.isTrue(map1.containsKey("conditionalKey"));
        Assert.isTrue(map1.get("conditionalKey").equals("yes"));
    }

}
