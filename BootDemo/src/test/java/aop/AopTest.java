package aop;

import cn.hutool.core.lang.Assert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;

@Data
class TestBean{
    private String property;

    public TestBean(String property) {
        this.property = property;
    }
}

@ImportAutoConfiguration(classes = {PropertyPlaceholderAutoConfiguration.class})
@PropertySource("classpath:/application.properties")
@Configuration
class ResolveConfiguration{

    @Qualifier("environment")
    @Autowired(required = false)
    Environment environment;

    @Resource
    Environment environmentl;


    @Configuration
    public static class NestProperty{
        @Value("${tmp_file:helloworld}")
        private String tmp;
        @Bean
        public TestBean testBean(){
            return new TestBean(tmp);
        }
    }

}

@Slf4j
public class AopTest {
    /**
     *  todo PropertySource
     */
    @Test
    public void test(){
        ArrayList<Object> objects = new ArrayList<>();

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(new Class<?>[]{ResolveConfiguration.class});
        context.refresh();;
        TestBean bean = context.getBean(TestBean.class);
        System.out.println(bean.getProperty());
        Arrays.stream(context.getBeanFactory().getSingletonNames()).forEach(System.out::println);
    }
//    @Test
//    public void aop(){
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeforeConfiguration.class);
//        AopConfig.UUIDConfig bean1 = context.getBean(AopConfig.UUIDConfig.class);
//        log.info("{}",bean1);
//        Environment bean = context.getBean(Environment.class);
//        log.info("{}",bean);
//    }
}

