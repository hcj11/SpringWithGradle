package BeanUtilsTest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactoryUtils;

public class SpringBeanUtils {
    @Getter
    @Setter
    public static class A{
     private String name;
        private String orderId;
    }
    @Getter
    @Setter
    public static class B{
        private String name;
    }

    public static void main(String[] args) {
        // ��α�֤���߳��µĵ���ģʽ��
        A a = new A();
        a.setName("hcj");
        a.setOrderId("111");
        B b = new B();
        BeanUtils.copyProperties(a,b);
        System.out.println(b.getName());
    }
}
