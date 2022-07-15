package autoconfiguration;

import context.CompontScan;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfigurationSubAndParentTest {

   @Disabled(value = "org.springframework.beans.factory.UnsatisfiedDependencyException:" +
           " Error creating bean with name 'test' defined in autoconfiguration.AutoConfigurationSubAndParentTest$AnothorCommon: " +
           "Unsatisfied dependency expressed through method " +
           "'test' parameter 0; nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'autoconfiguration.AutoConfigurationSubAndParentTest$Son'" +
           " available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {}\n")
    @org.junit.jupiter.api.Test
    public void Test() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AutoConfigurationSubAndParentTest.class);
        context.refresh();
        CompontScan.print(context);
    }

    static class Parent{}
    static class Son extends Parent{
    }
    static class Test{
        public Test(Son son) {
            Assertions.assertNull(son,"son must be null because of the parent bean dont autowire");;
        }
    }
    @Configuration
    static class Common{
        @Bean
        public Parent parent(){
            return new Parent();
        }
    }
    @Configuration
    static class AnothorCommon{
        @Bean
        public Test test(Son son){
            return new Test(son);
        }
    }
}
