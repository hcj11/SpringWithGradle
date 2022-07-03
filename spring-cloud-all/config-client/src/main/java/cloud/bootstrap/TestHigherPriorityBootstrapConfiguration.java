package cloud.bootstrap;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

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
