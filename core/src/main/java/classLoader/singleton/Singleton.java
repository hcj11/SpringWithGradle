package classLoader.singleton;

/**
 * 懒汉模式， 采用class 初始化,
 * 当调用class方法时，进行class的初始化，并赋值。
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
