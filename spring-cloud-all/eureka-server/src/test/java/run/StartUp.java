package run;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.HashMap;
import java.util.List;

public class StartUp {

    class Dummy{}
    @Test
    public void findFactory(){
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        List<String> list = SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, contextClassLoader);
        list.stream().forEach(System.out::println);

        HashMap<String, String> map = new HashMap<>();
        map.entrySet().stream().forEach((kv)->{
            String value = kv.getValue();
            String key = kv.getKey();

        });

    }

    @Test
    public void startup(){
        double a=1.1d;
        int a1 = (int) a;
        System.out.println(a1);
        double b =0.99;
        Assertions.assertEquals(Math.ceil(b),1.00 ,"1" );;
        double c=0.49;
        Assertions.assertEquals(Math.ceil(b),1.00 ,"2" );;
        double d=0.98;
        Assertions.assertEquals((int)d, 0,"3" );


    }

}
