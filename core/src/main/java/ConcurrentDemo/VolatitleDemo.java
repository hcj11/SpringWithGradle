package ConcurrentDemo;

import cn.hutool.core.lang.Assert;

import java.util.Arrays;
/**
 * 出现线程安全问题， 同时执行++，导致数值无变化，
 */
public class VolatitleDemo {
    public static volatile int race = 0;
    public static final int count = 20;

    public static void incr() {

//        int initrace = race;
//        race=initrace + 1;
        race ++;

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
        /**
         * race != 200000 ,
         */
    }
}
