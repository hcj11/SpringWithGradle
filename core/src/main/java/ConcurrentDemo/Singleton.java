package ConcurrentDemo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * DCL  双重检测锁的单例
 * // 由于volatile修饰的变量，只保证可见性，若出现依赖于原来操作的更新操作，那么必须使用同步块，来解决。
 */
public class Singleton {
    public static volatile Singleton singleton = null;
// 不满足，  运算结果不依赖当前状态的条件， （因为一个线程下，需要先判断是否已经实例化，来确定是否实例化，）
    public static Singleton getSingleton() {
        if (singleton == null) { // 那么当拿取时，立即可见
            synchronized (Singleton.class) {  // 若不设置synchronized 会导致赋值操作
                if (singleton == null) {
                    System.out.println(Thread.currentThread() + "进入");
                    singleton = new Singleton(); // 赋值后，立即刷新到主内存
                }
            }
        }
        return singleton;
    }

    public static void main(String[] args) {
//        Integer   private final int value;
//        Double
//        Long
//        BigDecimal
//        BigInteger
//        AtomicInteger //private volatile int value;
//        AtomicLong //    private volatile long value;
        HashSet<Singleton> singletons = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                singletons.add(getSingleton());
            }).start();
        }
        System.out.println(singletons.size());
        LongAdder longAdder = new LongAdder();
        longAdder.sumThenReset();

        longAdder.increment();;

    }
}
