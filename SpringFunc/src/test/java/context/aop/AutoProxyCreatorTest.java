package context.aop;

import cn.hutool.aop.aspects.SimpleAspect;
import cn.hutool.core.lang.Assert;
import domain.ITestBean;
import domain.TestBean;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

enum Gender {
    MAN("男"), FEMAN("女");
    private String name;

    Gender(String gender) {
        this.name = gender;
    }
}

public class AutoProxyCreatorTest {

    public static class CustomAspect extends SimpleAspect {
        @Override
        public boolean before(Object target, Method method, Object[] args) {
            return super.before(target, method, args);
        }
    }

    public static class SpringFactoryBean implements FactoryBean {
        private ITestBean testBean = new TestBean();

        /**
         * getBean() -> FactoryBean 构造的object，
         */
        @Override
        public Object getObject() throws Exception {
            ITestBean proxy1 = ProxyFactory.getProxy(ITestBean.class, new SingletonTargetSource(testBean));
            Assert.isFalse(AopUtils.isCglibProxy(proxy1));
            return proxy1;
        }

        @Override
        public Class<?> getObjectType() {
            return ITestBean.class;
        }

        @Override
        public boolean isSingleton() {
            return false;
        }
    }

    /**
     * 接口的动态代理， -> cblib or  接口代理。
     */
    public static class CustomFactoryBean implements FactoryBean<ITestBean> {
        private TestBean testBean = new TestBean();

        @Override
        public ITestBean getObject() throws Exception {
            ClassLoader classLoader = this.getClass().getClassLoader();
            return (ITestBean) Proxy.<ITestBean>newProxyInstance(classLoader, new Class[]{ITestBean.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return (ITestBean) ReflectionUtils.invokeMethod(method, testBean, args);
                }
            });
        }

        @Override
        public Class<?> getObjectType() {
            return ITestBean.class;
        }

        @Override
        public boolean isSingleton() {
            return false;
        }
    }

    public static class NoInterface {
    }

    @Test
    public void autoproxycreatorwithfallbackdynamicproxy() {
        StaticApplicationContext staticApplicationContext = new StaticApplicationContext();
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue("proxyFactoryBean", "false");

        staticApplicationContext.registerSingleton("messageSource", StaticMessageSource.class);
        staticApplicationContext.registerSingleton("noInterface", NoInterface.class);
        staticApplicationContext.registerSingleton("customAutoProxyCreator", CustomAutoProxyCreator.class, propertyValues);

        staticApplicationContext.registerSingleton("springNoInteceptor", SpringFactoryBean.class);
        staticApplicationContext.registerSingleton("springtoProxied", SpringFactoryBean.class);

        staticApplicationContext.registerSingleton("customtoProxied", CustomFactoryBean.class);
        staticApplicationContext.refresh();


        Object messageSource = staticApplicationContext.getBean("messageSource");
        ITestBean springNoInteceptor = (ITestBean) staticApplicationContext.getBean("springNoInteceptor");
        ITestBean springtoProxied = (ITestBean) staticApplicationContext.getBean("springtoProxied");
        ITestBean customtoProxied = (ITestBean) staticApplicationContext.getBean("customtoProxied");
        NoInterface noInterface = (NoInterface) staticApplicationContext.getBean("noInterface");

        Assert.isTrue(AopUtils.isCglibProxy(noInterface));
        Assert.isFalse(AopUtils.isCglibProxy(messageSource));
        Assert.isFalse(AopUtils.isCglibProxy(springNoInteceptor));
        Assert.isFalse(AopUtils.isCglibProxy(springtoProxied));
        Assert.isFalse(AopUtils.isCglibProxy(customtoProxied));

        CustomAutoProxyCreator customAutoProxyCreator = staticApplicationContext.getBean("customAutoProxyCreator", CustomAutoProxyCreator.class);

        Assert.isTrue(customAutoProxyCreator.getTestInterceptor().getInvocation() == 0);
        springNoInteceptor.getAge();
        Assert.isTrue(customAutoProxyCreator.getTestInterceptor().getInvocation() == 0);
        springtoProxied.getName();
        Assert.isTrue(customAutoProxyCreator.getTestInterceptor().getInvocation() == 1);
        springtoProxied.getSpouse();
        Assert.isTrue(customAutoProxyCreator.getTestInterceptor().getInvocation() == 2);

    }

}
