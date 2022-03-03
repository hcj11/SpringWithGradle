package aop;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import java.util.Map;
interface Using{
    void use();
}

@Slf4j
class CustomUsing implements Using{
    @Override
    public void use() {
       log.info("{}","just do it ");
    }
}

@Data
@Component
@Aspect
class Introducing{

    @DeclareParents(value = "aop.Shopping+" , defaultImpl = CustomUsing.class)
    public static  Using using;

}

@Order(2)
@Slf4j
@Component
@Aspect
public class UserAgent {
//    @Autowired
//    private AnnotationConfigApplicationContext applicationContext;

    int failCount =0 ;

    /**
     */
    @Pointcut(value = "execution(void aop.UserService.*(String)) && args(name)")
    public void pointcut(String name) {
    }

    @Pointcut(value = "execution(void aop.UserService.*(..))")
    public void pointcutAllArgs() {
    }
    @Pointcut(value = "execution(void aop.UserService.*(java.util.Map)) && args(map)")
    public void pointcutMap(Map map) {
    }

    @Before(value = "pointcut(name)")
    public void before(String name) {
        System.out.println("proxy" + name);
    }

    @Before(value = "pointcutMap(map)")
    public void before(Map map) {
        System.out.println("proxy" + map);
    }


    @Around(value = "pointcutAllArgs()")
    public void around(ProceedingJoinPoint proceedingJoinPoint) {
        System.out.println("before...");
        try {
            Object[] args = proceedingJoinPoint.getArgs();
            proceedingJoinPoint.proceed(args);
        } catch (Throwable throwable) {
            throw new RuntimeException("haha");
        }
        System.out.println("after...");
    }
    @AfterThrowing(value = "pointcut(name)")
    public void afterThrowing(String name){
        failCount++;
        System.out.println(failCount);
    }

}
