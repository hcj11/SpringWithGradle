package autoconfiguration;

import autoconfiguration.com.bao.SubA;
import context.CompontScan;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 to test the in case of the package order
 */
@ImportAutoConfiguration(value = {autoconfiguration.org.mybatis.SubB.class, SubA.class})
public class AutoConfigurationTest {
    @Test
    public void SubAIsBetterThanSubBBecauseOfThePackageOrder(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AutoConfiguration.class);
        context.registerBean("autoConfigurationTest", AutoConfigurationTest.class);
        context.refresh();
        CompontScan.printSingleton(context);
    }
}
