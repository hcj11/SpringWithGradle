package Initializer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class Initialzer {
    @Test
    public void demo1(){
        SpringBootServletInitializer springBootServletInitializer = new SpringBootServletInitializer(){
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                super.onStartup(servletContext);
            }
        };

    }
}
