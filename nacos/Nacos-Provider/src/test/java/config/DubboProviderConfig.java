package config;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.ServiceBean;
import org.apache.dubbo.config.spring.beans.factory.annotation.ServiceAnnotationBeanPostProcessor;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.dubbo.remoting.http.servlet.BootstrapListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

@EnableDubbo(scanBasePackages = "com.provider")
@Configuration
public class DubboProviderConfig {
   public static CountDownLatch countDownLatch = new CountDownLatch(1);

    @Order(value = Integer.MAX_VALUE)
    @EventListener(classes = ApplicationContextEvent.class)
    public void onApplicationContextEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            onContextRefreshedEvent((ContextRefreshedEvent) event);
        } else if (event instanceof ContextClosedEvent) {
            onContextClosedEvent((ContextClosedEvent) event);
        }
        countDownLatch.countDown();
    }

    private void onContextClosedEvent(ContextClosedEvent event) {
    }

    private void onContextRefreshedEvent(ContextRefreshedEvent event) {
    }

    @Bean("dubbo-demo-application")
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("dubbo-demo-application");
        return applicationConfig;
    }

    /**
     * Current registry center configuration, to replace XML config:
     * <prev>
     * &lt;dubbo:registry id="my-registry" address="N/A"/&gt;
     * </prev>
     *
     * @return {@link RegistryConfig} Bean
     */
    @Bean("my-registry")
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("N/A");
        return registryConfig;
    }

    /**
     * Current protocol configuration, to replace XML config:
     * <prev>
     * &lt;dubbo:protocol name="dubbo" port="12345"/&gt;
     * </prev>
     *
     * @return {@link ProtocolConfig} Bean
     */
    @Bean("dubbo")
    public ProtocolConfig protocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(12345);
        return protocolConfig;
    }
//    @Bean
//    public ServiceAnnotationBeanPostProcessor serviceAnnotationBeanPostProcessor(){
//        ServiceAnnotationBeanPostProcessor postProcessor = new ServiceAnnotationBeanPostProcessor("com.provider");
//        return postProcessor;
//    }
    // to export
    // wait to export . ServiceBeanPostProcessor


}
