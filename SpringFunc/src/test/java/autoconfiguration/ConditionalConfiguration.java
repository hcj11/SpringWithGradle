package autoconfiguration;

import context.CompontScan;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;

@Profile("v2")
@Configuration
class ConditionalConfiguration2 {
    @Bean
    public  A a(){return new A("v2");}
}
@Profile("v1")
@Configuration
class ConditionalConfiguration1 {
    @Bean
    public  A a(){return new A("v1");}
}
/**
 * mockito
 */
public class ConditionalConfiguration {

    @Test
    public void runConfiguration(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        ConfigurableEnvironment environment = context.getEnvironment();
        environment.setActiveProfiles("v1");
        context.register(ConditionalConfiguration2.class,ConditionalConfiguration1.class);
        context.refresh();
        CompontScan.printSingleton(context);
    }

}

