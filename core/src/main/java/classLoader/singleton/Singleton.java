package classLoader.singleton;

import java.util.ArrayList;

/**
 */
public class Singleton {
    private static A a;

    // Hoder.get()
    static class A {
        static {
            a = new A();
            System.out.println("thread: " + Thread.currentThread().getName());
        }

        static A getInstance() {
            return a;
        }
    }


    public static void main(String[] args) {


        for (int i = 0; i < 20; i++) {
            Thread thread1 = new Thread(() -> {
                System.out.println(A.getInstance());
            });
            thread1.start();
        }


    }
}
