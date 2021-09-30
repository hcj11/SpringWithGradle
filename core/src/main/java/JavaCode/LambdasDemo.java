package JavaCode;

import java.util.Arrays;

@FunctionalInterface
interface DoThisInterface {
    void process(int i, float j);
}

public class LambdasDemo {
    public LambdasDemo(){}
    static void process(int i, float j) {
        System.out.println("process");
    }

    public static void main(String[] args) {
        Arrays.sort(null, String::compareToIgnoreCase);
        ;
    }

    public void walk() {
        System.out.println("stand-in");
    }

    public void close() {
        System.out.println("stand-in...closed");
    }

    public static void demo4() {
//        LambdasDemo la = LambdasDemo::new;
    }

    /**
     * 引用： 具有特定类型的任意对象的实例方法
     */
    public static void demo3() {
        DoThisInterface func = LambdasDemo::process;
    }

    public static void demo1() {
        LambdasDemo lambdasDemo = new LambdasDemo();
        Runnable ran = lambdasDemo::walk;
        new Thread(ran).start();
    }

    public static void demo2() {
        LambdasDemo lambdasDemo = new LambdasDemo();
        try (AutoCloseable autoCloseable = lambdasDemo::close) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
