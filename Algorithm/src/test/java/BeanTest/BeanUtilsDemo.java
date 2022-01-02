package BeanTest;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

public class BeanUtilsDemo {
    @Getter
    @Setter
    public static class A {
        private String name;
        private String orderId;
        private final int a=0;

    }

    @Getter
    @Setter
    public static class B {
        private String orderId;

    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        A a = new A();
        a.setName("hello");
        a.setOrderId("222");
        A a2 = new A();
        //  class 的 public 找到修饰为准。
        BeanUtils.copyProperties(a2, a);
        System.out.println(a2.getOrderId());

        B b = new B();
        BeanUtils.copyProperties(b,a);
        System.out.println(b.getOrderId());

    }

}
