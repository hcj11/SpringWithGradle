package scan;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.config.spring.context.annotation.DubboClassPathBeanDefinitionScanner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import static org.springframework.context.annotation.AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR;
public class PackageScan {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PackageScan.class);
    /**
     * It'd better to use BeanNameGenerator instance that should reference
     * {@link ConfigurationClassPostProcessor#componentScanBeanNameGenerator},
     * thus it maybe a potential problem on bean name generation.
     *
     * @param registry {@link BeanDefinitionRegistry}
     * @return {@link BeanNameGenerator} instance
     * @see SingletonBeanRegistry
     * @see AnnotationConfigUtils#CONFIGURATION_BEAN_NAME_GENERATOR
     * @see ConfigurationClassPostProcessor#processConfigBeanDefinitions
     * @since 2.5.8
     */
    private BeanNameGenerator resolveBeanNameGenerator(BeanDefinitionRegistry registry) {

        BeanNameGenerator beanNameGenerator = null;

        if (registry instanceof SingletonBeanRegistry) {
            SingletonBeanRegistry singletonBeanRegistry = SingletonBeanRegistry.class.cast(registry);
            beanNameGenerator = (BeanNameGenerator) singletonBeanRegistry.getSingleton(CONFIGURATION_BEAN_NAME_GENERATOR);
        }

        if (beanNameGenerator == null) {

            if (logger.isInfoEnabled()) {

                logger.info("BeanNameGenerator bean can't be found in BeanFactory with name ["
                        + CONFIGURATION_BEAN_NAME_GENERATOR + "]");
                logger.info("BeanNameGenerator will be a instance of " +
                        AnnotationBeanNameGenerator.class.getName() +
                        " , it maybe a potential problem on bean name generation.");
            }

            beanNameGenerator = new AnnotationBeanNameGenerator();

        }

        return beanNameGenerator;

    }

//    @Test
//    public void scanTest(){
//        mock();
//
//        DubboClassPathBeanDefinitionScanner scanner =
//                new DubboClassPathBeanDefinitionScanner(registry, environment, resourceLoader);
//
//        BeanNameGenerator beanNameGenerator = resolveBeanNameGenerator(registry);
//
//        scanner.setBeanNameGenerator(beanNameGenerator);
//
//        scanner.addIncludeFilter(new AnnotationTypeFilter(Service.class));
//
//        /**
//         * Add the compatibility for legacy Dubbo's @Service
//         *
//         * The issue : https://github.com/apache/dubbo/issues/4330
//         * @since 2.7.3
//         */
//        scanner.addIncludeFilter(new AnnotationTypeFilter(com.alibaba.dubbo.config.annotation.Service.class));
//
//
//        // Registers @Service Bean first
//        scanner.scan(packageToScan);
//    }
}
