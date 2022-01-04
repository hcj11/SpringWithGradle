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
 *   extends  AbstractAutoProxyCreator  : SmartInstantiationAwareBeanPostProcessor
 *   createProxy()
 *
 *    1.methodInterceptor implements Advisor
 *    2.proxy create  targetSource
 *    3.
 *
 *
 *   1. @AspectJ   patterns
 *   2. AbstractAutoProxyCreator:       factoryBean or bean,
 *
 *   3. created from proxyfactoryBean,   bean
 *   4. target
 *
 *
 *
 *   aop   targetSource
 *   targetSource    commonpool .  druidpool
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
     *  ��������
     *  1. bean ����ʱ�� �� aop�������ȷ�ϡ� createProxy ,
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