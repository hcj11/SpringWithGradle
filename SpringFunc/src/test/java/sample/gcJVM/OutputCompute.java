

package sample.gcJVM;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

@Slf4j
class CustomArrayList<T> extends ArrayList<T> {
    @Override
    protected void finalize() throws Throwable {
        log.info("CustomArrayList --- finalize ");
    }
}

/**
 * 1) -ea -Xmx4g -Xms4g -Xmn512m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC
 * 2) -ea  -Xmx2g -Xms2g -Xmn1536m -XX:+UseParallelGC
 * <p>
 * 下次执行gc的finalize方法。
 */
@Slf4j
public class OutputCompute {

    public volatile CustomArrayList<byte[]> pigs = new CustomArrayList<byte[]>();
    public volatile WeakReference<CustomArrayList> weakReference = new WeakReference<CustomArrayList>(pigs, new ReferenceQueue<>()) {
        @Override
        public boolean enqueue() {
            return super.enqueue();
        }

        @Override
        protected void finalize() throws Throwable {
            log.error("WeakReference -- finalize");
        }
    };
    public volatile int pig_nums = 500;
    private volatile int queue_pigs;

    public OutputCompute() {

    }

    @Test
    public void printCpuProcessor() {
        int i = Runtime.getRuntime().availableProcessors();
        Assert.assertTrue(i == 4);

    }

    /**
     * -ea  -XX:+PrintGCDetails -XX:+UseSerialGC （年轻、老年代  串行） 复制 + 标记清除压缩算法。
     * -ea  -XX:+PrintGCDetails -XX:+UseParNewGC  MarkSweepCompact （年轻并行、老年代串行）
     * 3) -ea  -XX:+PrintGCDetails -XX:+UseParallelGC （年轻并行、老年代并行） 吞吐量最好的收集器
     * 4) -ea  -XX:+PrintGCDetails -XX:+UseParallelOldGC -XX:+UseParallelGC
     * 3) 和 4) 是相似的。
     * 5) -ea  -XX:+PrintGCDetails -XX:+UseConcMarkSweepGC  -XX:+UseParNewGC
     */
    @Test
    public void permTry1() throws InterruptedException {
        int size = 1024 * 1024;
        for (int i = 0; i < 1024; i++) {
            if (i % (1024 * 1024) == 0) {
                int circle = (i / (1024 * 1024));
                // 2048 批. 由于MaxPerm 不足，导致不断gc，释放空间， 执行效率降低
                log.info("===========第{}批", circle);
            }
            String.valueOf(i).intern();
        }
    }

    /**
     * 默认使用的 gc 策略以及对应的 gc是。。
     * <p>
     * -ea -XX:MaxPermSize=4M -XX:PermSize=2M  -XX:+PrintGCDetails
     * -ea
     */
    @Test
    public void perm() {
        // 2g
        int size = 1024 * 1024;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (i % (1024 * 1024) == 0) {
                int circle = (i / (1024 * 1024));
                // 2048 批. 由于MaxPerm 不足，导致不断gc，释放空间， 执行效率降低
                log.info("===========第{}批", circle);
            } // 运行时常量池，放到堆内。  class信息依然位于堆外内存。
            String.valueOf(i).intern();
        }
    }

    /**
     * 16384 /1024 =16 , 2:16 =  1:8   2:2
     * 16384k + 20480k = 36MB  + 4 MB
     * -ea -Xms40m -Xmn20m -Xmx40m -XX:+PrintGCDetails -XX:MaxTenuringThreshold=15 -XX:SurvivorRatio=8
     * 20MB ->
     * {
     * enden :  16 MB ,
     * survivor   from space: 2M
     * to space 2M
     * }
     * (1 + 19) /100 = 1/5 =20%
     * 2048 + 512;
     * -ea -Xmx40m -XX:+PrintGCDetails  -XX:+UseParallelGC  -XX:MaxGCPauseMillis=20  -XX:GCTimeRatio=19
     * -ea -Xmx40m -XX:+PrintGCDetails  -XX:+UseParallelGC  -XX:+UseAdaptiveSizePolicy
     * <p>
     * 吞吐量优先和性能优先。
     * -ea -Xmx40m -XX:+PrintGCDetails  -XX:+UseParallelGC  -XX:+UseAdaptiveSizePolicy -XX:+UseParallelOldGC
     * -ea -Xmx40m -XX:+PrintGCDetails  -XX:+UseParallelGC  -XX:+UseAdaptiveSizePolicy -XX:+UseParallelOldGC -XX:ParallelGCThreads=100
     * ==========================cms gc  减少暂停时间，提高响应能力。 提高吞吐量==================================
     * -ea -Xmx40m -XX:+PrintGCDetails  -XX:+UseConcMarkSweepGC -XX:ParallelGCThreads=100
     * -ea -Xmx40m -XX:+PrintGCDetails -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=1 -XX:CMSInitiatingOccupancyFraction=100  -XX:+UseConcMarkSweepGC -XX:ParallelGCThreads=100
     * -ea -Xmx40m -XX:+PrintGCDetails -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=20 -XX:CMSInitiatingOccupancyFraction=100  -XX:+UseConcMarkSweepGC -XX:ParallelGCThreads=640
     * out-of-memory 每个gc线程均摊的内存过大，超出总的堆内存，导致报错。
     * -ea -Xmx40m -XX:+PrintGCDetails -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=20 -XX:CMSInitiatingOccupancyFraction=100  -XX:+UseConcMarkSweepGC -XX:ParallelGCThreads=12800
     */
    @Test
    public void testGc() throws InterruptedException {
        byte[] b1 = new byte[1024 * 1024 / 2];
        byte[] b2 = new byte[1024 * 1024 * 8];
        b2 = null;
        b2 = new byte[1024 * 1024 / 8];
        System.gc();

        Thread.sleep(1090000);


    }

    class PigEaster extends Thread {
        private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PigEaster.class);

        @SneakyThrows
        @Override
        public void run() {

            log.info("PigEaster: {}", Thread.currentThread().getName());
            while (true) {
                //  32MB * queue_pigs
                pigs.add(new byte[32 * 1024 * 1024]);
                if (queue_pigs > pig_nums) {
                    log.error("PigEaster 吃饱了...");
                    return;
                }
                Thread.sleep(100);
            }
        }
    }

    class PigDigest extends Thread {
        private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PigDigest.class);

        @SneakyThrows
        @Override
        public void run() {
            log.info("PigDigest: {}", Thread.currentThread().getName());

            long start = System.currentTimeMillis();
            /**
             * 单线程执行 ++ 操作。不会受影响。  ，一个读一个写，不会影响结果。
             */
            while (true) {
                Thread.sleep(2000);
                queue_pigs += pigs.size();
                pigs = new CustomArrayList<>();
                if (queue_pigs > pig_nums) {
                    log.info("time interval(/s) : {}, pigs nums:{}",
                            (System.currentTimeMillis() - start) / 1000, queue_pigs);
                    return;
                    // default 5011 - 689627s           <1个 - 138s>
                    // 总结，当前电脑下差距不大。
                    // 1) time interval(/s) : 58, pigs nums:507
                    // 2) time interval(/s) : 54, pigs nums:506
                }
            }
        }
    }

    @Test
    public void start() throws InterruptedException {
//        new OutputCompute();
        PigEaster pigEaster = new PigEaster();
        pigEaster.setName("pigEaster");
        pigEaster.start();
        PigDigest pigDigest = new PigDigest();
        pigDigest.setName("pigDigest");
        pigDigest.start();
        Thread.sleep(1000000000);
    }
}