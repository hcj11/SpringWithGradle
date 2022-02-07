package sample;

import context.scan.CompontScan;
import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Data
@Configuration
public class SimpleConfig {
    public class MyBean {
    }

    @Component
    public  class OneFactoryBean implements FactoryBean<CompontScan.One> {
        public  void hello(){

        }
        @Override
        public CompontScan.One getObject() throws Exception {
            return SimpleConfig.this.one;
        }

        @Override
        public Class<?> getObjectType() {
            return CompontScan.One.class;
        }
    }

    @Autowired
    @Lazy
    public CompontScan.One one;

    @Autowired
    @Lazy
    public ObjectFactory<CompontScan.One> oneObjectFactory;

    @Autowired
    @Lazy
    public ObjectProvider<CompontScan.One> oneObjectProvider;

    @Lazy
    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

}