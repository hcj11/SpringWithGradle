package ConcurrentDemo;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 解决volatile 变量的原子性问题，
 * Atomic变量实现了原子性，      会存在ABA问题。
 */
public class AtomicDemo {
    public static AtomicInteger race = new AtomicInteger(0);
    public static int count = 20;

    public static void incr() {
        race.getAndIncrement();
    }

    public static void main(String[] args) {
        // 20 * 10000= 20 0000
        Thread[] threads = new Thread[count];
        for (int i = 0; i < count; i++) {
            threads[i] = new Thread(() -> {
                for (int ii = 0; ii < 10000; ii++) {
                    incr();
                }
            });
            threads[i].start();
            ;
        }
        Arrays.stream(threads).forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(race);
    }
}
