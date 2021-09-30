package Basic;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomPostProcess implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof E){
            E bean1 = (E) bean;
            bean1.name="hcj";
            return bean1;
        }
        return null; // 不做改动。
    }
}
