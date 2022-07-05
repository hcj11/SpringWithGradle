package spring;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Applications;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.Message;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.lang.management.ManagementFactory;


@Slf4j
@SpringBootTest(classes = {SpringDemoForPeerServer.Dummy.class},
        webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"debug=true"})
public class SpringDemoForPeerServer {
    @Autowired
    private ConfigurableApplicationContext context;
    @Autowired
    private ConfigurableEnvironment environment;
    @LocalServerPort
    public int port;
    Object lock = new Object();

    @EnableScheduling
    @ImportAutoConfiguration({ConfigFileApplicationListener.class,ScheduledTask.class})
    @EnableAutoConfiguration
    @EnableEurekaServer
    @Configuration
    public static class Dummy{

    }
    @Autowired
    EurekaClient discoveryClient;
    /**
     kotlin to bulid the gradle plugin
     */
    @Test
    public void demo1() throws InterruptedException {

        Applications applications = discoveryClient.getApplications();
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String property = environment.getProperty("spring.port");
        String spring_profiles_active = environment.getProperty("spring_profiles_active", String.class);
        Assertions.assertNotNull(spring_profiles_active);
        String spring_profiles_active2 = environment.getProperty("spring.profiles.active", String.class);
        Assertions.assertNotNull(spring_profiles_active2);

        log.info("startup web is port:{},pid:{},property:{}",port,name,property);
        synchronized (lock){lock.wait();}
    }
    /**
     * refreshScope test
     */
    @Slf4j
    @Configuration
    static class  ScheduledTask{
        @Scheduled(fixedRate = 5000)
        public void run(){
            String msg = message().getMsg();
            log.info("=======================current msg: {}",msg);
        }
        @RefreshScope
        @Bean
        public Message message(){
            return new Message();
        }
        @Data
        static class Message{
            @Value(value = "${refresh.message:nothing}")
            private String msg;
        }

    }




}
