package run;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.config.server.environment.CompositeEnvironmentRepository;
import org.springframework.cloud.config.server.environment.EnvironmentController;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import utils.Utils;

import java.net.InetAddress;
import java.net.InetSocketAddress;

@SpringBootTest(classes = StartUp.Dummy.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,properties = "server.port=8888")
public class StartUp {
    @LocalServerPort
    public int port;
    /**
     * start server for to  pull
     */
    Object lock = new Object();
    /**
     importance: the context is  `the parent of  AnnotationConfigApplicationContext` is
     */
    @Autowired
    GenericApplicationContext context ;
    @Autowired
    Environment environment;
    // how to load properties  from config server
    @Test
    public void loadPropertySourceOrder(){


        String property = environment.getProperty("a.b");
        Assertions.assertEquals(property,"zzzz");
        Assertions.assertNull(environment.getProperty("a.b.c"));
    }
    @Test
    public void startUp() throws InterruptedException {
        String address = environment.getProperty("server.address");

        String property = environment.getProperty("spring.datasource.driverClassName");
        Assertions.assertEquals(property,"com.mysql.jdbc.Driver");
        String peer2 = environment.getProperty("peer2");

//        Assertions.assertEquals(peer2,"localhost");
        System.out.println(String.format("address:%s,port:%s",address,port));
        Utils.print(context);
        synchronized (lock) {
            lock.wait();
        }
    }
    @EnableConfigServer
    @EnableAutoConfiguration
    @Configuration
    public static class Dummy {


    }

}
