package cloud.context.refresh;

import cloud.bootstrap.TestBootstrapConfiguration;
import cloud.bootstrap.TestHigherPriorityBootstrapConfiguration;
import cn.hutool.core.lang.Assert;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.cloud.bootstrap.BootstrapApplicationListener;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.*;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


@Slf4j
public class ContextRefreshTest {
    /**
     *
     */
    static RefreshScope refresh = mock(RefreshScope.class);

    public String getExternalProperitiesLocation() {

        return "classpath:/external-properties/bootstrap.properties";
    }

    @Test
    public void contextThreadLocal() {
        final String var1 = System.getProperty("java.class.path");
        log.info("{}", var1);
    }

    @Test
    public void fileLoad() throws IOException {
        String location = getExternalProperitiesLocation();
        try (ConfigurableApplicationContext context = SpringApplication.run(PropertySourceConfig.class,
                "--spring.main.web-application-type=none", "--debug=false", "--spring.profiles.active=local",
                "--spring.main.bannerMode=OFF",
                "--spring.cloud.bootstrap.addition-location=" + location)) {
            ConfigurableEnvironment environment = context.getEnvironment();
            assertEquals(environment.getProperty("servers[0].foo.bar"), "aaa");
            assertEquals(environment.getProperty("servers[1].far.boo"), "bbb");
            String[] property = environment.getProperty("redis.port", String[].class);
            assertNull(property);
            // Integer -> String
            int property1 = environment.getProperty("redis.port[0]", int.class);
            assertEquals(property1, 6379);

            assertEquals(environment.getProperty("redis.port[1]"), "8080");
            final int[] a = {0};
            final int[] b = {0};
            context.getEnvironment().getPropertySources().stream().forEach(pro -> {
                a[0]++;
                log.info("===1.={}", pro.getName());
            });

            PropertySource<Iterable<ConfigurationPropertySource>> configurationProperties = (PropertySource<Iterable<ConfigurationPropertySource>>) context.getEnvironment().getPropertySources().get("configurationProperties");
            Iterable<ConfigurationPropertySource> source = configurationProperties.getSource();
            source.forEach(pro -> {
                b[0]++;
                log.info("==2.=={}", pro.toString());
            });
            log.info("{},{}", a[0], b[0]);

        }
    }

    @Test
    public void pickUpAdditionalExternalProperties() {
        String location = getExternalProperitiesLocation();
        try (ConfigurableApplicationContext context = SpringApplication.run(Empty.class,
                "--spring.main.web-application-type=none", "--debug=false",
                "--spring.main.bannerMode=OFF",
                "--spring.cloud.bootstrap.addition-location=" + location)) {
            context.getEnvironment().getPropertySources().stream().forEach(pro -> {
                log.info("===={}", pro.getName());
            });
            String name = context.getEnvironment().getProperty("info.name");
            assertNotEquals("externalPropertiesName", name);
            String desc = context.getEnvironment().getProperty("info.desc");
            assertEquals("externalPropertiesDesc1", desc);
            String ces = context.getEnvironment().getProperty("info.ces");
            assertNull(ces);

        }
    }

    @Test
    public void pickupOnlyExternalProperties() {
        String location = getExternalProperitiesLocation();
        try (ConfigurableApplicationContext context = SpringApplication.run(Empty.class,
                "--spring.main.web-application-type=none", "--debug=false",
                "--spring.main.bannerMode=OFF",
                "--spring.cloud.bootstrap.location=" + location)) {
            String name = context.getEnvironment().getProperty("info.name");
            assertEquals("externalPropertiesName", name);
            String desc = context.getEnvironment().getProperty("info.desc");
            Assert.isNull(desc);
        }
    }

    @Test
    public void defaultActiveProperties() {
        try (ConfigurableApplicationContext context = SpringApplication.run(Empty.class,
                "--spring.main.web-application-type=none", "--debug=false",
                "--spring.main.bannerMode=OFF")) {
            List<String> names = names(context.getEnvironment().getPropertySources());
            ContextRefresher refresher = new ContextRefresher(context, refresh);
            refresher.refresh();
            names = names(context.getEnvironment().getPropertySources());
            then(names).doesNotContain(
                    "applicationConfig: [classpath:/bootstrap-refresh.properties]");
            then(names).contains(
                    "applicationConfig: [classpath:/application.properties]",
                    "applicationConfig: [classpath:/application.yml]",
                    "applicationConfig: [classpath:/bootstrap.properties]");
        }
    }

    @Test
    public void orderNewPropertiesConsistentWithNewContext() {
        try (ConfigurableApplicationContext context = SpringApplication.run(Empty.class,
                "--spring.main.web-application-type=none", "--debug=false",
                "--spring.main.bannerMode=OFF")) {
            context.getEnvironment().setActiveProfiles("refresh");
            List<String> names = names(context.getEnvironment().getPropertySources());
            then(names).doesNotContain(
                    "applicationConfig: [classpath:/bootstrap-refresh.properties]");
            ContextRefresher refresher = new ContextRefresher(context, refresh);
            refresher.refresh();
            names = names(context.getEnvironment().getPropertySources());
            then(names).contains(
                    "applicationConfig: [classpath:/bootstrap-refresh.properties]");
            then(names).containsAnyOf(
                    "applicationConfig: [classpath:/application.properties]",
                    "applicationConfig: [classpath:/application.yml]",
                    "applicationConfig: [classpath:/bootstrap-refresh.properties]",
                    "applicationConfig: [classpath:/bootstrap.properties]");
            /**
             * д╛хо  properties >  yml
             */
            String val = context.
                    getEnvironment().getProperty("test.bootstrap.foo");
            Assert.isTrue(val.equals("default_properties"));

            PropertySource<?> propertySource = context.getEnvironment().getPropertySources().get("applicationConfig: [classpath:/application.yml]");
            context.getEnvironment().getPropertySources().addFirst(propertySource);
            val = context.getEnvironment().getProperty("test.bootstrap.foo");
            /**
             * yml > properties
             */
            Assert.isTrue(val.equals("default_yml"));
        }

    }

    private List<String> names(MutablePropertySources propertySources) {
        List<String> list = new ArrayList<>();
        for (PropertySource<?> p : propertySources) {
            list.add(p.getName());
        }
        return list;
    }

    @org.springframework.context.annotation.PropertySource("classpath:application-dev.properties")
    @Configuration(proxyBeanMethods = false)
    protected static class PropertySourceConfig {

    }

    @Configuration(proxyBeanMethods = false)
    protected static class Empty {

    }

    @Configuration(proxyBeanMethods = false)
    // This is added to bootstrap context as a source in bootstrap.properties
    protected static class PropertySourceConfiguration implements PropertySourceLocator {

        public static Map<String, Object> MAP = new HashMap<>(
                Collections.<String, Object>singletonMap("bootstrap.foo", "refresh"));

        @Override
        public PropertySource<?> locate(Environment environment) {
            return new MapPropertySource("refreshTest", MAP);
        }

    }

    @Test
    public void commandLineArgsPassedToBootstrapConfiguration() {

        TestBootstrapConfiguration.firstCreated = Lists.<String>newArrayList();
        try (ConfigurableApplicationContext context = SpringApplication.run(ContextRefreshTest.class, new String[]{
                "--test.bootstrap.foo=bar",
                "--spring.cloud.bootstrap.name=refresh", "--spring.main.web-application-type=none"
                , "--debug=false", "--spring.main.bannerMode=OFF"
        })) {
            context.getEnvironment().setActiveProfiles("refresh");
            ContextRefresher contextRefresher = new ContextRefresher(context, refresh);
            Set<String> refresh = contextRefresher.refresh();
            log.info("===========refresh key:{}", refresh);
            then(TestBootstrapConfiguration.firstCreated).containsExactly("bar", "bar");
            then(TestHigherPriorityBootstrapConfiguration.count).hasValue(-2);
            then(TestHigherPriorityBootstrapConfiguration.firstRefrence).hasValue(TestHigherPriorityBootstrapConfiguration.class);
        }

        TestBootstrapConfiguration.firstCreated = null;
    }

}
