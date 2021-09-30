package Mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * 新增filter
 */
class CustomWebApplicationInitializer implements WebApplicationInitializer{
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addFilter("/web/*",CustomFilter.class);
    }
}

/**
 * 内嵌server 新增filter
 */
@WebFilter(urlPatterns = {"/web/*",})
class CustomFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("do  filter ");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
@WebServlet
class CustomServlet  extends HttpServlet {}
@WebListener
class CustomListener implements ServletContextListener {

}

@Data
class B{
    A a;
    public B(A a) {
        this.a=a;
    }
    void SayHello(){
        a.SayHello();
    }
}
@Data
class A{void SayHello(){}}
class SuperWebConfig{
    @Bean
    public A a(){
        return new A();
    }
}

@ServletComponentScan(basePackages = "Mvc")
@Slf4j
@Configuration
public class WebConfig extends SuperWebConfig implements WebMvcConfigurer {
    @Bean
    public  B b(@Qualifier("a") A a){
        return new B(a);
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomInteceptor());
    }

    /**
     * 启动时默认的类路径下面的路径定义、
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/test/**").addResourceLocations("file:/G:/upload/");

    }

    @Autowired
    public void taskExecutor(ThreadPoolTaskExecutor taskExecutor){
        this.taskExecutor=taskExecutor;
    };
    ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(5000);
        configurer.setTaskExecutor(taskExecutor);
        /**
         *  wait  vs LockPart.part(this)
         */

    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

//    AbstractAnnotationConfigDispatcherServletInitializer initializer = new AbstractAnnotationConfigDispatcherServletInitializer() {
//        @Override
//        protected Class<?>[] getRootConfigClasses() {
//            return new Class[0];
//        }
//
//        @Override
//        protected Class<?>[] getServletConfigClasses() {
//            return new Class[0];
//        }
//
//        @Override
//        protected String[] getServletMappings() {
//            return new String[0];
//        }
//    };

    @Accessors(chain = true)
    @Data
    static class CustomObj {
        private String key;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CustomObj customObj = (CustomObj) o;
            return Objects.equal(key, customObj.key);
        }

        @Override
        public int hashCode() {
            if (key.equalsIgnoreCase("kv1") || key.equalsIgnoreCase("kv2")) {
                return 0;
            }
            return System.identityHashCode(this);
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        /**
         *  hash key  val
         */
        CustomObj customObj1 = new CustomObj().setKey("kv1");
        CustomObj customObj2 = new CustomObj().setKey("kv2");

        HashMap<CustomObj, List<Integer>> map = Maps.<CustomObj, List<Integer>>newHashMap();
        map.put(customObj1, Lists.<Integer>newArrayList(0, 0));
        map.put(customObj2, Lists.<Integer>newArrayList(1, 0));

//        /**
//         * 根据key不会造成影响，只是数据的组合构造不同。 放到链表或者树中。
//         */
//        List<Integer> integers = map.get(customObj1);
//        log.info("{}", integers);
//        map.entrySet().forEach((kv) -> {
//            CustomObj key = kv.getKey();
//            List<Integer> value = kv.getValue();
//            System.out.println(value);
//        });
//        // key -> values
//        boolean kv1 = map.remove(customObj1, Lists.<Integer>newArrayList(0, 0));
//        System.out.println(kv1);
//        map.entrySet().forEach((kv) -> {
//            CustomObj key = kv.getKey();
//            List<Integer> value = kv.getValue();
//            System.out.println(value);
//        });

        ArrayList<HashMap<CustomObj, List<Integer>>> hashMaps = Lists.<HashMap<CustomObj, List<Integer>>>newArrayList();
        hashMaps.add(map);
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        log.info("{}", objectMapper.writeValueAsString(hashMaps));
        //  [{x:[],y:[]},{x:[],y:[]}]
        //  [{[],[]},{}]


    }

}
