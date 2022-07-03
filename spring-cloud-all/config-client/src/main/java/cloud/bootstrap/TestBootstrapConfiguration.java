package cloud.bootstrap;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Order(0)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties
public class TestBootstrapConfiguration implements ApplicationContextInitializer {

    public static List<String> firstCreated = null;

    public TestBootstrapConfiguration() {
        TestHigherPriorityBootstrapConfiguration.firstRefrence.
                compareAndSet(null, TestBootstrapConfiguration.class);
    }

    /**
     * get properties
     */
    @Bean
    @Qualifier(value = "test-bootstrap-foo")
    public String testBootstrapFoo(ConfigurableEnvironment configurableEnvironment, ApplicationEventPublisher publisher) {
        String property = configurableEnvironment.getProperty("test.bootstrap.foo", "undefined");
        if (firstCreated != null) {
            firstCreated.add(property);
        }
        return property;
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String key = environment.resolvePlaceholders("${spring.foo:bar}");

        HashMap<String, Object> map = Maps.<String, Object>newHashMap();
        map.put(key, "yes");
        MapPropertySource map1 = new MapPropertySource("test-epp-map", Collections.singletonMap("map1", map));
        environment.getPropertySources().addLast(map1);
    }

}
