package context.aop;

import domain.ITestBean;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.target.PrototypeTargetSource;
import org.springframework.aop.target.ThreadLocalTargetSource;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * AutoProxyCreator fallback to  dynamicproxy ,   aop ->  proxy of factory bean
 * AutoProxyCreator fallback to
 */
@Slf4j
class  CustomMethodBeforeAdvice implements MethodBeforeAdvice{

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        log.info("before method do this  inteceptor 1 ");
        ReflectionUtils.invokeMethod(method, target,args);
    }
}
@Slf4j
@Configuration
public class ProxyFactoryBean {

    @Bean
    public org.springframework.aop.framework.ProxyFactoryBean factoryBean(){

        org.springframework.aop.framework.ProxyFactoryBean proxyFactoryBean = new org.springframework.aop.framework.ProxyFactoryBean();
        proxyFactoryBean.setInterceptorNames();
        proxyFactoryBean.setInterfaces(ITestBean.class);
        proxyFactoryBean.setTarget(new ThreadLocalTargetSource());
        proxyFactoryBean.addAdvisor(new DefaultBeanFactoryPointcutAdvisor());
        proxyFactoryBean.addAdvisor(0,
                new DefaultPointcutAdvisor(new CustomMethodBeforeAdvice()));

        proxyFactoryBean.addAdvisor(1, new DefaultPointcutAdvisor(new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                log.info("before method do this  inteceptor 2 ");
                return invocation.proceed();
            }
        }));

        return proxyFactoryBean;
    }

    @Scheduled(cron = "")
    @Scheduled(cron = "")
    @Test
    public void demo1() {
        org.springframework.aop.framework.ProxyFactoryBean proxyFactoryBean = new org.springframework.aop.framework.ProxyFactoryBean();
        proxyFactoryBean.setInterceptorNames();
        proxyFactoryBean.setInterfaces(ITestBean.class);
        /**
         * targetSource.
         */
        PrototypeTargetSource prototypeTargetSource = new PrototypeTargetSource();
        prototypeTargetSource.getTarget();

        ThreadLocalTargetSource threadLocalTargetSource = new ThreadLocalTargetSource();
        threadLocalTargetSource.getTarget();

        proxyFactoryBean.setTarget(prototypeTargetSource);
        proxyFactoryBean.setTargetName("");

        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        Scheduler object1 = schedulerFactoryBean.getObject();

        /**
         *
         */

        /**
         * schedule
         * spring-quartz
         */

        /**
         * mybatis - spring   ���ɵķ�ʽ�� ��ˮ��  @Mapper ��ȡȻ�󹹽��������,������sql��pb������oracle
         *
         *
         */

        proxyFactoryBean.addAdvisor(new DefaultBeanFactoryPointcutAdvisor());
        proxyFactoryBean.addAdvisor(0,
                new DefaultPointcutAdvisor(new MethodBeforeAdvice() {
                    @Override
                    public void before(Method method, Object[] args, Object target) throws Throwable {

                    }
                }));
        proxyFactoryBean.addAdvisor(1, new DefaultPointcutAdvisor(new MethodBeforeAdvice() {
            @Override
            public void before(Method method, Object[] args, Object target) throws Throwable {
            }
        }));

        //        context;


        ITestBean object = (ITestBean) proxyFactoryBean.getObject();
    }


    public static void main(String[] args) {


    }

}
