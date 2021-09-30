package ProxyDemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 重写一遍代理模式，理解含义。
 */
public class Other {

    interface Hello {
        void sayHello();
    }

    static class HelloWorld implements Hello {

        @Override
        public void sayHello() {
            System.out.println("son say hello world");
        }
    }

    static class Proxy0 implements InvocationHandler {
        Object orignal;

        public Object bind(Object object) {
            this.orignal = object;
            Class<?>[] interfaces = object.getClass().getInterfaces();
            ClassLoader classLoader = object.getClass().getClassLoader();
            return Proxy.newProxyInstance(classLoader, interfaces, this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("before");
            // 严格确认提供的目标类的调用。否则容易出现栈溢出。
            Object invoke = method.invoke(orignal, args);
            System.out.println("bafter");
            return invoke;
        }
    }

    public static void main(String[] args) {
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles","true");
        Hello proxy0 = (Hello) new Other.Proxy0().bind(new HelloWorld());
        proxy0.sayHello();
    }
}
