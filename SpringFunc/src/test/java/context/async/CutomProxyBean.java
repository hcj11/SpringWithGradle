package context.async;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Async;

import java.lang.reflect.Method;
import java.util.Arrays;


@Slf4j
@EnableAspectJAutoProxy
@Configuration
public class CutomProxyBean  {


    @Autowired
    private AsyncResult asyncResult;

    @Bean
    public ProxyFactoryBean proxyFactoryBean(){
        /**
         * 都是代理，拦截
         */
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(asyncResult);
        proxyFactoryBean.setProxyTargetClass(true);
        proxyFactoryBean.setSingleton(true);

        AnnotationMatchingPointcut annotationMatchingPointcut = new AnnotationMatchingPointcut(null,Async.class);

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(annotationMatchingPointcut);

        advisor.setAdvice(new MethodBeforeAdvice() {
            @Override
            public void before(Method method, Object[] args, Object target) throws Throwable {
                if(method.getName().contains("hello")){
                    Object[] ints = {1, 2, 3};
                    String s = Arrays.deepToString(ints);
                    String s1 = Arrays.deepToString(args);
                    log.info("before : {},{}",s,s1);
                }
            }
        });
        proxyFactoryBean.addAdvisor(advisor);
        return proxyFactoryBean;
    }
    public void demo1(){
    }
    public static void main(String[] args) throws NoSuchMethodException {
        Method hello = AsyncResult.class.getDeclaredMethod("hello", String[].class);
        AnnotationMatchingPointcut annotationMatchingPointcut = new AnnotationMatchingPointcut(Async.class);
        boolean matches = annotationMatchingPointcut.getMethodMatcher().matches(hello,AsyncResult.class);
        Assert.isTrue(matches);
    }

}
