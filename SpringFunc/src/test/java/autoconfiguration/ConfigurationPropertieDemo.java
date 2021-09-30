package autoconfiguration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * ����ʹ�á� ConfigurationProperties �� EnableConfigurationProperties ��ϵ��
 */
@Data
@ConfigurationProperties(prefix = "hello")
class Demo{
    Properties properties;
}

@Configuration
@EnableConfigurationProperties({Demo.class})
public class ConfigurationPropertieDemo {
    @Autowired
    private Demo demo;

    @ConfigurationProperties(prefix = "hello")
    @Bean
    public Properties demo(){
        return new Properties();
    }

}
