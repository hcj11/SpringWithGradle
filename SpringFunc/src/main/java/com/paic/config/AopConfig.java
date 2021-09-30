package com.paic.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@Data
@ConfigurationProperties(prefix = "tmp")
class Tmp{
    public String files;

}

@Slf4j
@Data
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Configuration
public class AopConfig {
    @Configuration
    public static class BeanFactoryHelper{
        public BeanFactoryHelper(BeanFactory beanFactory) {
        }
    }
    @Configuration
    public static class UUIDGen implements BeanPostProcessor  {
        @Value("${tmp.files}")
        public String files;
        @Autowired
        Tmp tmp;


        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            return null;
        }

        static class  SubBeanFactoryHelper extends BeanFactoryHelper implements BeanFactoryAware{
            BeanFactory beanFactory;
            public SubBeanFactoryHelper(BeanFactory beanFactory) {
                super(beanFactory);
            }
            @Override
            public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
                this.beanFactory = beanFactory ;
            }
        }
    }
}
