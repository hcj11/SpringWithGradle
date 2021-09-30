package ConcurrentDemo;


import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class synchronizedDemo {
    /**
     * Vector size add remove
     *
     */
    private static Vector<String> strings = new Vector<>();
    // Exception in thread "Thread-2768" java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 11
    public static void main(String[] args) {
        // Exception in thread "main" java.lang.SecurityException: Unsafe
        // boolean b = Unsafe.getUnsafe().compareAndSwapInt(1, 1, 1, 1);

        while (true) {
            new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    strings.add(i + "");
                }
            }).start();
            new Thread(() -> {
                for (int i = 0; i < strings.size(); i++) {
                    strings.remove(i);
                }
            }).start();
            new Thread(() -> {
                for (int i = 0; i < strings.size(); i++) {
                    strings.get(i);
                }
            }).start();
            //
            while (Thread.activeCount() > 20) ;
        }


    }
}
