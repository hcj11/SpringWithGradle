package sample;

import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.*;
import java.util.HashMap;

public class CustomImport {
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Import(value = CustomImportWare.class)
    public @interface EnableSomeConfiguration {
        String value();
    }

    @Configuration
    @EnableSomeConfiguration("foo")
    public static class ConfigurationOne {
    }

    @Configuration
    @Conditional(value = OnMissingBeanCondition.class)
    @EnableSomeConfiguration("bar")
    public static class ConfigurationTwo {
    }

    public static class OnMissingBeanCondition implements ConfigurationCondition {

        @Override
        public ConfigurationPhase getConfigurationPhase() {
            return ConfigurationPhase.REGISTER_BEAN;
        }

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return (context.getBeanFactory().getBeanNamesForType(CustomImportWare.MetaHolder.class, true, false)).length == 0;
        }
    }

}
