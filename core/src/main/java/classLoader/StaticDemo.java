package classLoader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.ReflectUtils;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

/*
 ���������ػ���
* @author  houchunjian
* @date  2020/11/28 0028 14:44
* @param null
* @return
*/
interface CustomInterface {
    public default void initial() {
        System.out.println(" parent initial");
    }
}

class CustomInterfaceImpl implements CustomInterface {
    @Override
    public void initial() {
        System.out.println(" son  initial");
    }
}

/**
 * �����׶Σ� ���ݷ�������ȥ���� class or interface , ���� �ֶν��� ���� �������� , �ӿڷ����Ľ�����
 * �ֶν���
 * 1. �������ϣ� �������ѯ
 * 2. �Ƚӿڵݹ��ѯ����class �ݹ��ѯ
 *
 * ��������
 * 1. �������ϣ� �������ѯ
 * 2. ��class�ݹ��ѯ����ӿڵݹ��ѯ
 *
 * �ӿڷ������� ->  �ȴӸ��ӿ�ȥ����-> �ӽӿ�
 *
 * vs  ��ĳ�ʼ���׶Σ�   ���֡����Գ�����=
 *
 */
interface ParentAInterface{
    static  int value=1;
    public void sayHello();
}
  class ParentA {
    static {
        System.out.println("�ȴ�������ĳ�ʼ��");
    }
    static int value = 123;
    public ParentA(int feature){};
      public void sayHello(){};

  }
class A extends ParentA implements ParentAInterface{
    static {
        System.out.println("��������ĳ�ʼ��");
    }
    static int value=2;
    public A(){super(1);}
    public void sayHi() {
        sayHello();
    }
}

class SonStatic extends StaticDemo{
    final  String a="";
}
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StaticDemo implements Cloneable {
    private String account;
    final  String a = "";

    static {
        System.out.println("new ��ʼ����,ֻ��ʼ��һ��");
    }

    static void hello() {
        log.info("{}", "hello world");
    }

    /**
     * ���� class �ĳ�ʼ��
     */
    public static void demo1() throws CloneNotSupportedException, NoSuchMethodException {
        StaticDemo staticDemo = new StaticDemo("hcj");

        Object clone = staticDemo.clone();

        StaticDemo staticDemo1 = new StaticDemo("hcj");

        Method declaredMethod = ReflectUtils.findDeclaredMethod(StaticDemo.class, "hello", null);
        A a = new A();
        /**
         *  since 1.7   MethodHandles dynamic language support
         */
        MethodHandles.lookup();
        CustomInterfaceImpl customInterface = new CustomInterfaceImpl();
        customInterface.initial();
        ;


    }
    /**
     * -XX:+TraceClassLoading
     */
    public static void main(String[] args) throws CloneNotSupportedException, NoSuchMethodException {
        log.info("{}", A.value);
        /**
         *  idea �Ĵ�����ı������� ȷ����ص��߼���
         */

    }
}
