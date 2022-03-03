package spring.load;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import utils.Utils;

@EnableConfigurationProperties
@NoArgsConstructor
class CustomApplicationContextInitializerC implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(org.springframework.context.ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(environment,"tempfile=f:\\repo");
    }
}
@Data
@SpringBootTest(classes = {PropertiesLoad.Dummy.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(initializers = {CustomApplicationContextInitializerC.class})
public class PropertiesLoad {
    @Autowired
    private ConfigurableApplicationContext context;
    @Value("${temp_file}")
    public String temp_file;

    @Configuration
    @PropertySource(value = "/application.properties")
    public static class Dummy{}

    @Test
    public void demo1(){
        Utils.print(context);
        // tempfile
        Assertions.assertTrue(temp_file.equals("f:\repo"));
    }
}
