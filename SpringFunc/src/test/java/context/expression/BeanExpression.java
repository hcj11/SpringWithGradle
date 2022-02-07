package context.expression;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import context.CompontScan;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ConfigurationProperties
@Data
class NoPrefixCustomMap {
    Map<String, String> map;
}

@Data
class ConfigurationCls{
    private String name;
}
@ConfigurationProperties(prefix = "outercls")
@Data
class CustomOuterBean {
    private String val;
    @NestedConfigurationProperty
    private ConfigurationCls configurationCls;
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
@ConfigurationProperties(prefix = "disable")
@Data
class DisableBean {
    private String dummy;
}
@Data
class OpenBean {
    private String dummy;
}
@Slf4j
@RunWith(SpringRunner.class)
public class BeanExpression {

    @Data
    @PropertySource(value = "classpath:/application.yml")
    @PropertySource(value = "classpath:/application.properties")
    @EnableConfigurationProperties(value = {CustomOuterBean.class,MybatisPlusProperties.class,CustomMap.class, Dummy.class,DisableBean.class})
    @Configuration(value = "testbean")
    static class TestBean {

        @Value("${spring.profiles.active:no}")
        String active;

        @Value("${number:0}")
        Integer number;
/**
 warnning: you need in case of the  bean to ensure the variable 's  type
 */
        @ConditionalOnExpression("#{environment.getProperty('spring.profiles.active').equals('dev') " +
                "and environment.getProperty('number').equals('11111') " +
                "}")
        @Bean
        public DisableBean disableBean() {
            return new DisableBean();
        }
        @ConditionalOnExpression("#{environment.getProperty('spring.profiles.active').equals('dev') " +
                "and environment.getProperty('number').equals('1111') " +
                "}")
        @Bean
        public OpenBean openBean() {
            return new OpenBean();
        }
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

    AnnotationConfigApplicationContext context = null;

    BeanExpressionResolver resolver = null;
    BeanExpressionContext expressionContext = null;
    ConfigurableListableBeanFactory beanFactory = null;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        // configFile for parse the yaml file
        context = new AnnotationConfigApplicationContext();
        ConfigFileApplicationContextInitializer configFileApplicationContextInitializer = new ConfigFileApplicationContextInitializer();
        configFileApplicationContextInitializer.initialize(context);
        context.register(ConfigFileApplicationListener.class,TestBean.class);
        context.refresh();

        context.getEnvironment().getPropertySources().forEach(propertySource -> {
            log.info("=={}", propertySource.toString());
        });

        beanFactory = context.getBeanFactory();

        CompontScan.print(beanFactory);

        resolver = beanFactory.getBeanExpressionResolver();
        // StandardBeanExpressionResolver

        expressionContext = new BeanExpressionContext(beanFactory, null);

    }
    private YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
    @Test
    void loadOriginAware() throws Exception {

        Resource resource = new ClassPathResource("/application.yml", getClass());
        List<org.springframework.core.env.PropertySource<?>> loaded = this.loader.load("resource", resource);
        for (org.springframework.core.env.PropertySource<?> source : loaded) {
            EnumerablePropertySource<?> enumerableSource = (EnumerablePropertySource<?>) source;
            for (String name : enumerableSource.getPropertyNames()) {
                System.out.println(name + " = " + enumerableSource.getProperty(name));
            }
        }
    }


    @Test
    public void readYaml(){

        String yamlStr ="/application.yaml";
        Yaml yaml = new Yaml();
        Object load = yaml.load(yamlStr);
        log.info("CustomOuterBean:{}",load);
    }
    /**
     need load YamlPropertySourceLoader (in the ConfigFileApplicationListener)  firstly!
     */
    @Test
    public void loadPropertiesWithNestedFromYml(){
        CustomOuterBean bean = context.getBean(CustomOuterBean.class);
        log.info("CustomOuterBean:{}",bean);

    }
    @Test
    public void loadPropertiesWithNestedFromProperties(){
        // load Properties.
        CustomOuterBean bean = context.getBean(CustomOuterBean.class);
        log.info("CustomOuterBean:{}",bean);
        MybatisPlusProperties mybatisPlusProperties = context.getBean(MybatisPlusProperties.class);
        log.info("MybatisPlusProperties:{}",mybatisPlusProperties);

    }
    @Test
    public void loadResourceInOrderFromYml() throws IOException {
        String resolvedLocation = "classpath:/application.yml";
        loadResourceInOrder(resolvedLocation);
    }

    public void loadResourceInOrder(String resolvedLocation) throws IOException {

        ResourceLoader resourceLoader = new DefaultResourceLoader(com.paic.Config.class.getClassLoader());
        Resource resource = resourceLoader.getResource(resolvedLocation);

        URI uri = resource.getURI();

        ResourcePropertySource resourcePropertySource = new ResourcePropertySource(resource);

        log.info("{},{}", uri.toString(), resourcePropertySource.toString());
        /**
         * uri: F:\integration\basic\SpringWithGradle\SpringFunc\src\main\resources
         */
    }
    @Test
    public void MultiBeanWithSameType(){
        Map<String, Dummy> beansOfType = context.getBeansOfType(Dummy.class);
        log.info("map:{}",beansOfType);
        Set<String> strings = beansOfType.keySet();
        Assert.assertEquals(strings.toArray(new String[0]),new String[]{"dummy","dummy1-context.expression.Dummy"});
        Assert.assertTrue(beansOfType.get("dummy")!=beansOfType.get("dummy1-context.expression.Dummy"));
        Assert.assertEquals(beansOfType.get("dummy"),beansOfType.get("dummy1-context.expression.Dummy"));
        Assert.assertThrows(NoUniqueBeanDefinitionException.class,()->{context.getBean(Dummy.class);});

    }
    @Test
    public void ConditionalEvaluateTestToDisableDisableBean() {
        // when DisableBean don't register in the beanfactory
        // and use the ConfigurationPropertiesBean,
        DisableBean bean = context.getBean("disable-context.expression.DisableBean", DisableBean.class);
        Assert.assertTrue(bean.getDummy().equals("foo"));
    }

    @Test
    public void expressionEvaluateMultiConditionInTheLocal() {
        Environment bean = beanFactory.getBean(Environment.class);
        String property = bean.getProperty("spring.profiles.active");
        Assert.assertTrue(property.equals("dev"));
        // from the all( environment)  and  the part of all (systemEnvironment、systemProperties)
        // the name of PropertySource
        //  to get property,
        Boolean evaluate = (Boolean) resolver.evaluate("#{environment.getProperty('spring.profiles.active').equals('dev')" +
                " and  environment.getProperty('number').equals('1111') " +
                " and systemEnvironment.get('USERDOMAIN_ROAMINGPROFILE').equals('0JRXX56OU4ME1C4')" +
                " and environment.getProperty('NUMBER_OF_PROCESSORS').equals('4') }", expressionContext);
        Assert.assertTrue(evaluate);


    }

    /**
     * context/BeanExpression-context.xml
     */
    @Test
    public void expressionEvaluate() {


        Boolean evaluateActive = (Boolean) resolver.evaluate("#{testbean.active.equals('dev')}", expressionContext);
        Assert.assertTrue(evaluateActive);
        Boolean evaluateNumer = (Boolean) resolver.evaluate("#{testbean.number.equals(1111)}", expressionContext);
        Assert.assertTrue(evaluateNumer);

        Dummy dummy = (Dummy) beanFactory.getBean("dummy1-context.expression.Dummy");

        Assert.assertEquals(dummy.msg, "hello,scope!");

        Object evaluate0 = resolver.evaluate("#{noPrefixCustomMap.map.get('spring.profiles.active').equals('dev')}", expressionContext);

        Object evaluate1 = resolver.evaluate("#{dummy.msg}", expressionContext);

        Object evaluate11 = resolver.evaluate("#{dummy.msg.equals('hello,scope!')}", expressionContext);

        CustomMap customMap = (CustomMap) beanFactory.getBean("customMap");

        Map<String, String> map = (Map<String, String>) customMap.getMap();

        CustomMap customMap2 = (CustomMap) beanFactory.getBean("customMap");

        log.info("==customMap2: ={}", customMap2);
        /**
         *  systemProperties.
         */
        Object evaluate2 = resolver.evaluate("#{customMap.map.get('spring.profiles.active').equals('dev')}", expressionContext);

        log.info("===================one: {}, two: {}, three: {}", evaluate1, evaluate2, evaluate11);

    }


}
