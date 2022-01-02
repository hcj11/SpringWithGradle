package autoconfiguration.org.mybatis;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Data
class BeanB{}
@Configuration
public class SubB {
    @Bean
    public BeanB beanB(){
        return new BeanB();
    }
}
