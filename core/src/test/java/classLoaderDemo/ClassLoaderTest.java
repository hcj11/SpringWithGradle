package classLoaderDemo;


import org.junit.jupiter.api.Test;

/**
 * 在多线程中，jvm可以保证class 初始化中保证一次只有一个线程可以执行，并且只初始化一次 。 并且当加载失败后，、
 * 同属一个class加载器的其他线程不会重新进入初始化，
 */
public class ClassLoaderTest {

    static class Parent {
        static {
            if (true) {
                System.out.println("init class");
                while (true) {
                }
            }
        }

        // 读取class的变量进行初始化操作
        private static int a = 1;

    }
    @Test
    public void test1(){
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                System.out.println("one: " + Thread.currentThread().getName());
                System.out.println(Parent.a);
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                System.out.println("two: " + Thread.currentThread().getName());
                System.out.println(Parent.a);
            }
        });
        thread1.start();
        thread2.start();
    }
    /**
     * one: Thread-0
     * two: Thread-1
     * init class
     *
     * 无法执行赋值操作，而第二个线程必须等待，第一个线程初始化后才能取值
     */
}
