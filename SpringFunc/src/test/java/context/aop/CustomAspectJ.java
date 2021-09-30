package context.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;


@EnableAspectJAutoProxy
class AopConfig{}

@Component
@Aspect
public class CustomAspectJ {
    @Pointcut("")
    void perform(){}

    @Pointcut
    void others(){}

    @Before(value = "perform()")
    public void start(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
    }
    @Around(value = "perform()")
    public void around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            proceedingJoinPoint.proceed();

        } catch (Throwable throwable) {
            /**
             *  ß∞‹÷ÿ ‘°£
             */
            Object proceed = proceedingJoinPoint.proceed();
        }
    }
    @After(value = "others()")
    public void after(){}
}

