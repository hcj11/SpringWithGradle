package cloud.bootstrap;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration(proxyBeanMethods = false)
public class TestHigherPriorityBootstrapConfiguration {
   public static AtomicInteger count = new AtomicInteger(0);
   public static AtomicReference<Class> firstRefrence = new AtomicReference<>();

    /**
     * get properties
     */
    public  TestHigherPriorityBootstrapConfiguration(){
        count.getAndDecrement();
        firstRefrence.compareAndSet(null,TestHigherPriorityBootstrapConfiguration.class);
    }


}
