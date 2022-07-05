package utils;

import lombok.Data;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
@Data
public class Utils {
    public static  AtomicReference<ConfigurableEnvironment> reference = new AtomicReference<>();
    /**
     * you can use the 发号器 use the redis.
     */
    public static   AtomicInteger serverPort = new AtomicInteger(12345);

    public static String zookeeperHost = System.getProperty("zookeeper.address", "127.0.0.1");
    public static void printSingleton(GenericApplicationContext context) {
        Stream.of(context.getDefaultListableBeanFactory().getSingletonNames()).forEach(System.out::println);
    }
    public static void print(ListableBeanFactory context) {

        Stream.of(context.getBeanDefinitionNames()).forEach(System.out::println);
    }
    public static List<String> names(MutablePropertySources propertySources) {

        List<String> list = new ArrayList<>();
        for (PropertySource<?> p : propertySources) {
            list.add(p.getName());
        }
        return list;
    }
}