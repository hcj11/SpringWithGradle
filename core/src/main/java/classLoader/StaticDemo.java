package classLoader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.ReflectUtils;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

/*
 虚拟机类加载机制
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
 * 解析阶段， 根据符号引用去解析 class or interface , 或者 字段解析 或者 方法解析 , 接口方法的解析。
 * 字段解析
 * 1. 自下向上， 先自身查询
 * 2. 先接口递归查询，后class 递归查询
 *
 * 方法解析
 * 1. 自下向上， 先自身查询
 * 2. 先class递归查询，后接口递归查询
 *
 * 接口方法解析 ->  先从父接口去加载-> 子接口
 *
 * vs  类的初始化阶段，   区分。面试常考。=
 *
 */
interface ParentAInterface{
    static  int value=1;
    public void sayHello();
}
  class ParentA {
    static {
        System.out.println("先触发父类的初始化");
    }
    static int value = 123;
    public ParentA(int feature){};
      public void sayHello(){};

  }
class A extends ParentA implements ParentAInterface{
    static {
        System.out.println("触发子类的初始化");
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
        System.out.println("new 初始化类,只初始化一次");
    }

    static void hello() {
        log.info("{}", "hello world");
    }

    /**
     * 触发 class 的初始化
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
         *  idea 的代码的文本分析。 确定相关的逻辑。
         */

    }
}
