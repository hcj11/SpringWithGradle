package context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.ServletContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SwaggerDocumentTest {
    @Autowired
    ServletContext servletContext;

    @Test
    public void get() {
//        SpringApplicationBuilder builder = new SpringApplicationBuilder(TestBean.class);
//        try(ConfigurableApplicationContext context = builder.run("")){
//                context.getBean("");
//        }

    }

    @Configuration
    @EnableAutoConfiguration
    static class TestBean {
    }
}
