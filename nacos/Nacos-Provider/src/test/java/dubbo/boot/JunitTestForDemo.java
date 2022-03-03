package dubbo.boot;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.RegistryConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.mock.env.MockPropertySource;
import org.springframework.test.context.ContextConfiguration;

class CustomApplicationContextInitializerA implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private  JunitTestForDemo junitTestForDemo;
    public CustomApplicationContextInitializerA(){}
    public CustomApplicationContextInitializerA(JunitTestForDemo junitTestForDemo){
        this.junitTestForDemo =  junitTestForDemo ;
    }
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
    }
}
/**
 start the service
 */
public class JunitTestForDemo {


    @BeforeEach
    public void setUp() throws Exception {

    }
    @Test
    public void try1(){
        CustomApplicationContextInitializerA customApplicationContextInitializer1 = BeanUtils.instantiateClass(CustomApplicationContextInitializerA.class);
        CustomApplicationContextInitializerA customApplicationContextInitializer = new CustomApplicationContextInitializerA();

    }
}
