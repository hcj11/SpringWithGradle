package ProxyDemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyUsing {
    interface Hello {
        void sayHello();
    }

    static class Son implements Hello {

        @Override
        public void sayHello() {
            System.out.println("son say hello world!!!");
        }
    }

    static class ProxyManager implements InvocationHandler {
        Object orignalObj;

        // ���ɶ�̬����
        public Object bind(Son son) {
            this.orignalObj = son;
            ClassLoader classLoader = son.getClass().getClassLoader();
            Class<?>[] interfaces = son.getClass().getInterfaces();
            return Proxy.newProxyInstance(classLoader, interfaces, this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("before");
            // ����Ŀ��� ִ�з���
            Object invoke = method.invoke(orignalObj, args);
            System.out.println("after");
            return invoke;
        }
    }


    public static void main(String[] args) {

//        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles","true");

        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Son son = new Son();
        Hello bind = (Hello) new ProxyManager().bind(new Son());
        bind.sayHello();

    }
}
