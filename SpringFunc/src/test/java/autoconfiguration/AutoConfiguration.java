package autoconfiguration;

import context.CompontScan;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;


@Slf4j
@Data
class A {
    public A(String v2) {
        log.info("A .. init,{}", v2);
    }
}

@Slf4j
@Data
class B {
    public B() {
        log.info("B .. init");
    }
}

/**
 * 扫描configuration顺序。
 *
 * @AutoConfigureOrder -> @AutoConfigureBefore
 */
@AutoConfigureOrder(value = 1)
@Configuration(proxyBeanMethods = false)
class SubA {
    @Bean
    public A a() {
        return new A("v2");
    }
}

@Slf4j
class CustomConditional implements ConfigurationCondition {

    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return ConfigurationPhase.REGISTER_BEAN;
    }

    /**
     * 根据@Conditional 对应class下的注解进行configuration的确认
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return true;
    }
}
/**
 *  @ConditionalOnProperty(name = "name",havingValue = "hcjhcj")
   in case of @AutoConfigureBefore have influence to other bean autowired.

 */
//
@ConditionalOnProperty(name = "name",havingValue = "hcjhcj")
@Configuration(proxyBeanMethods = false)
@AutoConfigureOrder(value = 2)
@AutoConfigureBefore(SubA.class)
@Conditional(value = {CustomConditional.class})
class SubB {
    @Bean
    public B b() {
        return new B();
    }
}
@ImportAutoConfiguration({SubB.class})
public class AutoConfiguration {
    private String name;

    @Configuration
    class SubC {
        private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SubC.class);

        public void getParentName() {
            log.info("{}", AutoConfiguration.this.name);
        }
    }

    public static void main(String[] args) {

    }
    @Test
    public void AutoConfigureBeforeAnnoBetterThanOrder() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AutoConfiguration.class);
        context.registerBean("subc", SubC.class);
        context.refresh();
        CompontScan.printSingleton(context);

    }

    @Test
    public void before() {

    }
}
