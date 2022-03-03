package context.context.properties;

import cn.hutool.core.lang.Assert;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(value = {ConfigurationProperties.AA.class})
public class ConfigurationProperties {

    @Configuration
    @EnableConfigurationProperties(value = {PrefixAA.class})
    static class PrefixConfigurationProperties {
    }

    @Configuration
    @EnableConfigurationProperties(value = {TestMap.class})
    static class MapConfigurationProperties {
    }

    @Data
    @Configuration
    @EnableConfigurationProperties
    @org.springframework.boot.context.properties.ConfigurationProperties(prefix = "test")
    static class MapIntegerConfigurationProperties {
        private Map<Integer, Map<String, String>> map;
    }

    @Data
    @Configuration
    @EnableConfigurationProperties
    static class ExampleConfigurationProperties {

        @org.springframework.boot.context.properties.ConfigurationProperties(prefix = "example")
        @Scope(value = "prototype")
        @Bean
        public PrototypeBean prototypeBean() {
            return new PrototypeBean();
        }

        @Data
        static class PrototypeBean {
            private String one;
            private String two;
        }
    }

    @org.springframework.boot.context.properties.ConfigurationProperties(prefix = "aa")
    @Data
    static class AA {
        private String name;
    }

    @org.springframework.boot.context.properties.ConfigurationProperties("spring.foo")
    @Data
    static class PrefixAA {
        private String name;

    }

    @org.springframework.boot.context.properties.ConfigurationProperties("testmap")
    @Data
    static class TestMap {
        private Map<String, String> map;
    }


    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

    @AfterEach
    void cleanup() {
        this.context.close();
        System.clearProperty("name");
        System.clearProperty("nested.name");
        System.clearProperty("nested_name");
    }

    private AnnotationConfigApplicationContext load(Class<?> configuration, String... inlinedProperties) {
        return load(new Class<?>[]{configuration}, inlinedProperties);
    }

    private AnnotationConfigApplicationContext load(Class<?>[] configuration, String... inlinedProperties) {
        this.context.register(configuration);
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(this.context, inlinedProperties);
        this.context.refresh();
        return this.context;
    }

    @Test
    public void loadBind() {
        AnnotationConfigApplicationContext load = load(ConfigurationProperties.class, "name=foo", "user=hcj");
        AA bean = load.getBean(AA.class);
        Assert.notNull(bean);
        Assert.isTrue(bean.name.equalsIgnoreCase("foo"));
    }

    @Test
    public void loadPrefixBind() {
        AnnotationConfigApplicationContext load = load(PrefixConfigurationProperties.class, "spring.foo.name=hcjhcj", "user=hcj");
        PrefixAA bean = load.getBean(PrefixAA.class);
        Assert.notNull(bean);
        Assert.isTrue(bean.name.equalsIgnoreCase("hcjhcj"));
    }

    @Test
    public void loadMapBind() {
        AnnotationConfigApplicationContext load = load(MapConfigurationProperties.class, "testmap.map.name=hcjhcj", "user=hcj");
        TestMap bean = load.getBean(TestMap.class);
        Assert.notNull(bean);
        Assert.isTrue(bean.getMap().get("name").equalsIgnoreCase("hcjhcj"));
    }

    @Test
    public void loadIntegerMapBind() {
        AnnotationConfigApplicationContext load = load(MapIntegerConfigurationProperties.class, "test.map.1.name=hcjhcj", "user=hcj");
        MapIntegerConfigurationProperties bean = load.getBean(MapIntegerConfigurationProperties.class);
        Assert.notNull(bean);
        Assert.isTrue(bean.getMap().get(1).get("name").equalsIgnoreCase("hcjhcj"));
    }
    /**
     *   ConfigurationProperties
     */
    @Test
    public void loadShouldSupportRebinderConfigurationProperties() {
        MutablePropertySources propertySources = context.getEnvironment().getPropertySources();
        HashMap<String,Object> source = Maps.newHashMap();
        source.put("example.one","1");
        PropertySource listPropertySource = new MapPropertySource("convention", source);
        propertySources.addFirst(listPropertySource);
        // -----------------------------------
        context.register(ExampleConfigurationProperties.class);
        context.refresh();
        Assert.isTrue(context.getBean(ExampleConfigurationProperties.class).prototypeBean().getOne()
                .equals("1"));

        source.put("example.one","2");
        propertySources.addFirst(new MapPropertySource("extra", source));

        Assert.isTrue(context.getBean(ExampleConfigurationProperties.class).prototypeBean().getOne()
                .equalsIgnoreCase("2"));
    }

}
