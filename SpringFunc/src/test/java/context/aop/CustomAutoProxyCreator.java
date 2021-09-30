package context.aop;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import lombok.Data;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.target.CommonsPool2TargetSource;
import org.springframework.aop.target.ThreadLocalTargetSource;
import org.springframework.aop.target.ThreadLocalTargetSourceStats;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.support.StaticMessageSource;

/**
 *   extends  AbstractAutoProxyCreator  :  共同使用 SmartInstantiationAwareBeanPostProcessor 特性 创建bean代理， 进行拦截处理。
 *   createProxy()
 *    组件:
 *    1.methodInterceptor implements Advisor
 *    2.proxy create  targetSource
 *    3.
 *
 *
 *   1. @AspectJ   patterns 确定位置。
 *   2. AbstractAutoProxyCreator:        拦截factoryBean or bean, 监控方法调用次数。 从context,获取creator,确定调用次数。
 *
 *   3. created from proxyfactoryBean,   监控bean调用次数。
 *   4. target
 *
 *
 *
 *   aop  作为代理工厂。  用注解或者 targetSource 进行拦截和监控。
 *   targetSource   监控比如 commonpool .  druidpool
 *
 *
 *  extends AbstractAutoProxyCreator{}
 *
 *
 */
public class CustomAutoProxyCreator extends AbstractAutoProxyCreator {
    private boolean proxyFactoryBean = true;
    private boolean proxyObject = true;
    public static TestInterceptor testInterceptor = new TestInterceptor();

    public static TestInterceptor getTestInterceptor() {
        return testInterceptor;
    }

    public void setProxyFactoryBean(boolean proxyFactoryBean) {
        this.proxyFactoryBean = proxyFactoryBean;
    }

    public void setProxyObject(boolean proxyObject) {
        this.proxyObject = proxyObject;
    }

    public CustomAutoProxyCreator() {
        setProxyFactoryBean(true);
        setOrder(0);
    }

    @Override
    protected Object createProxy(Class<?> beanClass, String beanName, Object[] specificInterceptors, TargetSource targetSource) {
        return super.createProxy(beanClass, beanName, specificInterceptors, targetSource);
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
    /**
     *  触发流程
     *  1. bean 构建时， 由 aop代理进行确认。 createProxy ,
     *
     */
        if (StaticMessageSource.class.isAssignableFrom(beanClass)) {
            return DO_NOT_PROXY;
        } else if (beanName.endsWith("toProxied")) {
            boolean assignableFrom = FactoryBean.class.isAssignableFrom(beanClass);
            if ((assignableFrom && proxyFactoryBean) || (!assignableFrom && proxyObject)) {
                return new Object[]{testInterceptor};
            } else {
                return DO_NOT_PROXY;
            }
        } else {
            return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
        }

    }
}