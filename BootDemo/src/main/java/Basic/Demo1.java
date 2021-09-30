package Basic;

import com.google.common.collect.Lists;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.loader.WebappClassLoader;
import org.apache.tomcat.util.digester.Digester;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.annotation.AsyncConfigurationSelector;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.Set;
import java.util.WeakHashMap;
@PropertySource(value = "classpath:/application.properties")
@EnableAsync
@Configuration
public class Demo1 {


    public void demo1(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(CarFactoryBean.class);
        context.refresh();
        // 注册+刷新上下文、
        Car car = (Car)context.getBean(Car.class);
        System.out.println(car);
        Car car2 = (Car)context.getBean(Car.class);
        System.out.println(car);
        System.out.println(car==car2);
    }
    // 默认通过allowEarlyReference 解决循环依赖问题。
    public static void demo2(){
        // allowCircularReferences
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(B.class,A.class);
        B bean1 = context.getBean(B.class);
        A bean2 = context.getBean(A.class);
        System.out.println(bean1);
        System.out.println(bean2);
    }
    public static void demo3(){
        // 果然出错 Is there an unresolvable circular reference?
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setAllowCircularReferences(false);
        context.register(B.class,A.class);
        context.refresh();
        B bean1 = context.getBean(B.class);
        A bean2 = context.getBean(A.class);
        System.out.println(bean1);
        System.out.println(bean2);
    }
    public static void demo4(){
        // 果然出错 Is there an unresolvable circular reference?    // 若是原型类也会出现报错，
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(B.class,A.class);
        context.refresh();
        B bean1 = context.getBean(B.class);
        A bean2 = context.getBean(A.class);
        System.out.println(bean1);
        System.out.println(bean2);
    }
    public static void demo5(){
        // 果然出错 Is there an unresolvable circular reference? ,对于构造器模式下，也会出现该问题
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(C.class,D.class);
        context.refresh();
        C bean1 = context.getBean(C.class);
        D bean2 = context.getBean(D.class);
        System.out.println(bean1);
        System.out.println(bean2);
    }
    public static void demo6(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(E.class,F.class,CustomPostProcess.class);
        context.refresh();
        E bean1 = context.getBean(E.class);
        F bean2 = context.getBean(F.class);
        System.out.println(bean1.name); // 后置注入
        System.out.println(bean2==null); // false

    }
    public static void demo7(){
        GenericApplicationContext context = new GenericApplicationContext();
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(context);
        scanner.scan("basic");
        int beanDefinitionCount = context.getBeanDefinitionCount();
        System.out.println(beanDefinitionCount);
        context.refresh();
        E bean1 = context.getBean(E.class);
        System.out.println(bean1);

    }
    private static WeakHashMap<ClassLoader, String> map = new WeakHashMap<>();

    public static void memoryLeakTest() {
        ArrayList<String> list = Lists.<String>newArrayList();
        System.gc();
        map.entrySet().forEach((kv)->{
            ClassLoader key = kv.getKey();
            if(((WebappClassLoader)key).getState()== LifecycleState.STARTED){
                boolean add = list.add(kv.getValue());
            };
        });
        System.out.println(list);



    }
    public static void other(){
        ArrayList<String> list = Lists.newArrayList("a", "b", "c");
        String s = StringUtils.collectionToDelimitedString(list, ",", "'", "'");
        System.out.println(s);
        Set<String> strings = StringUtils.commaDelimitedListToSet("'a','b','c'");
        System.out.println(strings);
    }

    public static void main(String[] args) {
        demo2();
    }
}
