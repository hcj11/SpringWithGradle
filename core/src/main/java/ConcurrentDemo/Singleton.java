package ConcurrentDemo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * DCL  ˫�ؼ�����ĵ���
 * // ����volatile���εı�����ֻ��֤�ɼ��ԣ�������������ԭ�������ĸ��²�������ô����ʹ��ͬ���飬�������
 */
public class Singleton {
    public static volatile Singleton singleton = null;
// �����㣬  ��������������ǰ״̬�������� ����Ϊһ���߳��£���Ҫ���ж��Ƿ��Ѿ�ʵ��������ȷ���Ƿ�ʵ��������
    public static Singleton getSingleton() {
        if (singleton == null) { // ��ô����ȡʱ�������ɼ�
            synchronized (Singleton.class) {  // ��������synchronized �ᵼ�¸�ֵ����
                if (singleton == null) {
                    System.out.println(Thread.currentThread() + "����");
                    singleton = new Singleton(); // ��ֵ������ˢ�µ����ڴ�
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
