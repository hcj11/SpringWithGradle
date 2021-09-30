package context;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
@Lazy
@Slf4j
//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
class A {
    @Autowired
    private B b;
    @Async
    public void async() {
        log.info("{},async ... ",Thread.currentThread().getName());
    }
}
@Lazy
@Slf4j
//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
class B {
    @Autowired
    private A a;
    public void try1(){
        a.async();
    }

}
@Lazy
@Component
class E {
    @Autowired
    private A a;
    public void try1(){
        a.async();
    }

}
@Component
class D {
    private C c;

    public D(C c) {
        this.c = c;
    }
}

@Component
class C {
    private D d;

    public C(D d) {
        this.d = d;
    }
}

@ImportAutoConfiguration(value = TaskExecutionAutoConfiguration.class)
@PropertySource(value = "classpath:/application.properties")
@EnableAsync
@Configuration
public class CircleRefence {
//    @Bean
//    public ThreadPoolTaskExecutor taskExecutor(){
//        return new ThreadPoolTaskExecutor();
//    }

    @Test
    public void demo1() throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(CircleRefence.class, E.class , A.class, B.class);
        CompontScan.print(context);
        B bean = context.getBean(B.class);
        bean.try1();
        Thread.sleep(1000000);


    }

}