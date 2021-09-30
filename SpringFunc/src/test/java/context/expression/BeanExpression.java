package context.expression;

import context.CompontScan;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;

@ConfigurationProperties
@Data
class NoPrefixCustomMap {
    Map<String, String> map;
}

// dummy1-context.Dummy
@ConfigurationProperties(prefix = "custom")
@Data
class CustomMap {
    Map<String, String> map;
}

@ConfigurationProperties(prefix = "dummy1")
@Data
class Dummy {
    String msg;
}


@Slf4j
@RunWith(SpringRunner.class)
public class BeanExpression {

    @Data
    @PropertySource(value = "classpath:/application.properties")
    @EnableConfigurationProperties(value = {CustomMap.class, Dummy.class})
    @Configuration(value = "testbean")
    static class TestBean {

        @Value("${spring.profiles.active:no}")
        String active;

        @Value("${number:0}")
        Integer number;

        @Bean
        public Dummy dummy() {

            return new Dummy();
        }

        @Bean
        public CustomMap customMap() {
            return new CustomMap();
        }

        @Bean
        public NoPrefixCustomMap noPrefixCustomMap() {
            return new NoPrefixCustomMap();
        }
    }

    @Before
    public void setUp() {
    }

    @Test
    public void loadResourceInOrder() throws IOException {

        ResourceLoader resourceLoader = new DefaultResourceLoader(com.paic.Config.class.getClassLoader());
        String resolvedLocation = "classpath:/application.properties";
        Resource resource = resourceLoader.getResource(resolvedLocation);

        URI uri = resource.getURI();

        ResourcePropertySource resourcePropertySource = new ResourcePropertySource(resource);

        log.info("{},{}", uri.toString(), resourcePropertySource.toString());
        /**
         * uri: F:\integration\basic\SpringWithGradle\SpringFunc\src\main\resources
         */
    }

    /**
     * context/BeanExpression-context.xml
     */
    @Test
    public void expressionEvaluate() {
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(TestBean.class);
        context.getEnvironment().getPropertySources().forEach(propertySource -> {
            log.info("=={}", propertySource.toString());
        });


        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

        CompontScan.print(beanFactory);

        BeanExpressionResolver resolver = beanFactory.getBeanExpressionResolver();

        BeanExpressionContext expressionContext = new BeanExpressionContext(beanFactory, null);


        Boolean evaluateActive = (Boolean) resolver.evaluate("#{testbean.active.equals('dev')}", expressionContext);
        Assert.assertTrue(evaluateActive);
        Boolean evaluateNumer = (Boolean) resolver.evaluate("#{testbean.number.equals(1111)}", expressionContext);
        Assert.assertTrue(evaluateNumer);

        Dummy dummy = (Dummy) beanFactory.getBean("dummy1-context.Dummy");

        Assert.assertEquals(dummy.msg, "hello,scope!");

        Object evaluate0 = resolver.evaluate("#{noPrefixCustomMap.map.get('spring.profiles.active').equals('dev')}", expressionContext);

        Object evaluate1 = resolver.evaluate("#{dummy.msg}", expressionContext);

        Object evaluate11 = resolver.evaluate("#{dummy.msg.equals('hello,scope!')}", expressionContext);

        CustomMap customMap = (CustomMap) beanFactory.getBean("customMap");

        Map<String, String> map = (Map<String, String>) customMap.getMap();

        CustomMap customMap2 = (CustomMap) beanFactory.getBean("custom-context.CustomMap");

        log.info("==customMap2: ={}", customMap2);
        /**
         *  systemProperties.
         */
        Object evaluate2 = resolver.evaluate("#{customMap.map.get('spring.profiles.active').equals('dev')}", expressionContext);

        log.info("===================one: {}, two: {}, three: {}", evaluate1, evaluate2, evaluate11);

    }


}
