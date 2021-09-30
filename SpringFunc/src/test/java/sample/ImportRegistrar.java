package sample;

import cn.hutool.core.lang.Assert;
import lombok.Data;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

@Data
class TestBean {
    private String username;
}

public class ImportRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private Environment environment;
    boolean called=false;

    @Configuration
    public static class CustomA {
        @Bean
        public TestBean testBean() {
            return new TestBean();
        }

    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        registry.registerBeanDefinition("oneBeans", new RootBeanDefinition(String.class));

        registry.registerBeanDefinition("customA", new RootBeanDefinition(CustomA.class));

        Assert.state(!called," method call twice");

        called=true;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
