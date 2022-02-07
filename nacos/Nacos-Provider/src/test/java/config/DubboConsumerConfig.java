package config;

import com.api.UserInterface;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.spring.beans.factory.annotation.ReferenceAnnotationBeanPostProcessor;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.junit.jupiter.api.Assertions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableDubbo(scanBasePackageClasses = DubboConsumerConfig.class, multipleConfig = true)
@Configuration
public class DubboConsumerConfig {
    @Bean("dubbo-demo-application")
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("dubbo-demo-application");
        return applicationConfig;
    }

    /**
     * Current registry center configuration, to replace XML config:
     * <prev>
     * &lt;dubbo:registry address="N/A"/&gt;
     * </prev>
     *
     * @return {@link RegistryConfig} Bean
     */
    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("N/A");
        return registryConfig;
    }

    @Reference(version = "2.5.7", url = "dubbo://localhost:12345?version=2.5.7",application ="dubbo-demo-application" ,lazy = true)
    public UserInterface userInterface;

    @Reference(version = "2.5.7", url = "dubbo://localhost:12345?version=2.5.7",application ="dubbo-demo-application")
    public UserInterface userInterfaceNotLazy;

    @Reference(version = "2.5.7", application ="dubbo-demo-application")
    public UserInterface userInterfaceDefalut;

    @Reference(version = "2.5.7", url = "injvm://localhost:12345?version=2.5.7",application ="dubbo-demo-application")
    public UserInterface userInterfaceInJvm;


    public String getName() {
        // and use  shareClient.
        String name = userInterface.getName();
        String name1 = userInterfaceNotLazy.getName();
        String name2 = userInterfaceDefalut.getName();
        String name3 = userInterfaceInJvm.getName();
        String[] strings = {name,name1,name2,name3};
        String[] strings2 = {"hcjForNewDubbo","hcjForNewDubbo","hcjForNewDubbo","hcjForNewDubbo"};
        Assertions.assertArrayEquals(strings,strings2);
        return name;
    }

}
