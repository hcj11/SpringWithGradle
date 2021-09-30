package aop.cglib;

import org.springframework.cglib.core.Signature;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class EnhancerDemo {

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        Object o = enhancer.create();
        MethodInterceptor methodInterceptor = new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                method.invoke(o, objects);
                Signature signature = methodProxy.getSignature();
                String internalName = signature.getReturnType().getInternalName();

                return null;
            }
        };


    }
}