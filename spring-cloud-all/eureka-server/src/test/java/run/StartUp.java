package run;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

public class StartUp {

    class Dummy{}
    @Test
    public void findFactory(){
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        List<String> list = SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, contextClassLoader);
        list.stream().forEach(System.out::println);

    }
    @Test
    public void startup(){


    }

}
