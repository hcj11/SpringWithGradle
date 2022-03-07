package context.test;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import context.CompontScan;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;

@Slf4j
@SpringBootTest(classes = {TestAnnoationWithDirtiesMethodBySpringBootTestContextBootstrapper.Dummy.class})
public class TestAnnoationWithDirtiesMethodBySpringBootTestContextBootstrapper  implements InitializingBean{

    @Autowired
    private ConfigurableApplicationContext applicationContext;
    @Autowired
    private ConfigurableEnvironment environment;
    @Autowired
    private Bean bean;

    @Override
    public void afterPropertiesSet() throws Exception {
        // org.springframework.beans.factory
        // setting logging level
        log.info("reinitializing");
    }

    @Data
    public static class Bean{
        private String name;
    }

    @Import(LoggingApplicationListener.class)
    @Configuration
    public static class Dummy{
        public Dummy(){super();}
        @org.springframework.context.annotation.Bean
        public Bean bean(){
            return new Bean();
        }
    }
    @DirtiesContext
    @Test
    public void demo1(){
        String property = environment.getProperty("spring.profiles.active", String.class);

        CompontScan.print(applicationContext);
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(environment,"name=hcj");
        Bean bean = applicationContext.getBean(Bean.class);
        bean.setName("hcj");
    }
    @DirtiesContext
    @Test
    public void demo2(){
        String name = environment.getProperty("name", String.class);
        Assertions.assertNull(name);

        Bean bean = applicationContext.getBean(Bean.class);
        Assertions.assertNull(bean.getName());
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(environment,"name=hcj");


    }
    @Test
    public void demo3(){
        String name = environment.getProperty("name", String.class);
        Assertions.assertNull(name);

        Assertions.assertNull(bean.getName());
    }
}
