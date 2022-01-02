package autoconfiguration.com.bao;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Data
class BeanA{}
@Configuration
public class SubA {
    @Bean
    public BeanA beanA(){
        return new BeanA();
    }
}
