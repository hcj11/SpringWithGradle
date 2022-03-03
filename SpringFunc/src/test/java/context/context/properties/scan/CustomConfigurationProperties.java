package context.context.properties.scan;

import context.CompontScan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 *  to test the @EnableConfigurationProperties scan the bean .
 *  use the 1ConfigurationPropertiesBindingPostProcessor
 */

@SpringBootTest(classes = CustomConfigurationProperties.Dummy.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CustomConfigurationProperties {
/**
 private static Elements elementsOf(CharSequence name, boolean returnNullIfInvalid, int parserCapacity) {
 */
    public static class CustomFilter implements TypeFilter {
        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
            return true;
        }
    }

    @ComponentScan(useDefaultFilters = false,basePackages ={"context.context.properties.scan"}
    ,includeFilters = {@ComponentScan.Filter(
             value = {CustomFilter.class},
            type = FilterType.CUSTOM)})
    @EnableConfigurationProperties
    @Configuration
    public static class Dummy{}
    @Autowired
    private GenericApplicationContext context;
    Object lock = new Object();
    @Test
    public void scan() throws InterruptedException {

        CompontScan.print(context);
        synchronized (lock){lock.wait();}
    }
}
