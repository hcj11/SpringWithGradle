package refresh;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import utils.Utils;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ContextRefreshIntegrationTest.TestBeanConfiguration.class})
public class ContextRefreshIntegrationTest {
    @Autowired
    private TestProperties testProperties;
    @Autowired
    private Environment environment;
    @Autowired
    private ConfigurableApplicationContext applicationContext;
    RefreshScope mock = mock(RefreshScope.class);
    @Autowired
    private ContextRefresher contextRefresher;

    @Test
    public void refreshProperties() {

        Utils.print(applicationContext);

        String msg = testProperties.getMsg();
        assertEquals(msg, "hello,scope!");
        testProperties.setMsg("got it !!!");
        ContextRefresher contextRefresher = new ContextRefresher(applicationContext, mock);
        Set<String> refresh = contextRefresher.refresh();
        Assertions.assertEquals(refresh.size(),0,"should be 0");
        assertEquals(msg, "hello,scope!");
    }

    @EnableConfigurationProperties(value = TestProperties.class)
    @Configuration
    @EnableAutoConfiguration
    static class TestBeanConfiguration {

    }

    @ConfigurationProperties
    @Data
    static class TestProperties {
        private String msg;
    }
}
