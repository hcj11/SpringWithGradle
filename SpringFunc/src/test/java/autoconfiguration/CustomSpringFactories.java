package autoconfiguration;

import context.CompontScan;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;
@Slf4j
@Configuration
public class CustomSpringFactories {

    /**
     * Return the class used by {@link SpringFactoriesLoader} to load configuration
     * candidates.
     *
     * @return the factory class
     */
    protected Class<?> getSpringFactoriesLoaderFactoryClass() {
        return EnableAutoConfiguration.class;
    }
    @org.junit.Test
    public void com(){
        SpringApplicationBuilder builder  = new SpringApplicationBuilder();
        builder.build("").run("");


    }

    @org.junit.Test
    public void scan(){
        /**
         * @ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
         *  @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
         *  useDefaultFilter  =>  扫描 @component
         */
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(context,true);
        scanner.addExcludeFilter(new TypeExcludeFilter());
        scanner.addExcludeFilter(new AutoConfigurationExcludeFilter());

        int autoconfiguration = scanner.scan("autoconfiguration");
        log.info("=====scan componet num, {}",autoconfiguration);
        CompontScan.print(context);
    }

    /**
     * kotlin  compile  test
     */
    @Test
    public void demo() {

        List<String> configurations =
                SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),
                        CustomSpringFactories.class.getClassLoader());
        log.info("{}",configurations);

        /**		String factoryTypeName = factoryType.getName();
            return loadSpringFactories(classLoader).getOrDefault(factoryTypeName, Collections.emptyList());
         *
         */

//        ServletWebServerFactoryAutoConfiguration.class;
//        ServletContext.class;



    }
}
