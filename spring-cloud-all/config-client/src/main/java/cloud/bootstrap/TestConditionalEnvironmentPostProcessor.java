package cloud.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Collections;
import java.util.HashMap;

public class TestConditionalEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if(!environment.getPropertySources().contains("test-epp-map")){
            HashMap<String, Object> map = new HashMap<String,Object>();
            map.put("conditionalKey","yes");
            MapPropertySource map1 = new MapPropertySource("test-epp-map",
                    Collections.singletonMap("map1", map));
            environment.getPropertySources().addFirst(map1);
        };
    }
}
