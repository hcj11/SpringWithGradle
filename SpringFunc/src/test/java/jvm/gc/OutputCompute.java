package jvm.gc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.oxm.xstream.XStreamMarshaller;

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
 * �´�ִ��gc��finalize������
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
    public void printCpuProcessor(){
        int i = Runtime.getRuntime().availableProcessors();
        Assert.assertTrue(i==4);

    }
    /**
     * -ea  -XX:+PrintGCDetails -XX:+UseSerialGC �����ᡢ�����  ���У� ���� + ������ѹ���㷨��
     * -ea  -XX:+PrintGCDetails -XX:+UseParNewGC  MarkSweepCompact �����Ტ�С���������У�
     * 3) -ea  -XX:+PrintGCDetails -XX:+UseParallelGC �����Ტ�С���������У� ��������õ��ռ���
     * 4) -ea  -XX:+PrintGCDetails -XX:+UseParallelOldGC -XX:+UseParallelGC
     * 3) �� 4) �����Ƶġ�
     * 5) -ea  -XX:+PrintGCDetails -XX:+UseConcMarkSweepGC  -XX:+UseParNewGC
     */
    @Test
    public void permTry1() throws InterruptedException {
        int size = 1024 * 1024;
        for (int i = 0; i < 1024; i++) {
            if (i % (1024 * 1024) == 0) {
                int circle = (i / (1024 * 1024));
                // 2048 ��. ����MaxPerm ���㣬���²���gc���ͷſռ䣬 ִ��Ч�ʽ���
                log.info("===========��{}��", circle);
            }
            String.valueOf(i).intern();
        }
    }

    /**Ĭ��ʹ�õ� gc �����Լ���Ӧ�� gc�ǡ���
     *
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
                // 2048 ��. ����MaxPerm ���㣬���²���gc���ͷſռ䣬 ִ��Ч�ʽ���
                log.info("===========��{}��", circle);
            } // ����ʱ�����أ��ŵ����ڡ�  class��Ϣ��Ȼλ�ڶ����ڴ档
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
     * 2048 + 512;
     * -ea -Xmx40m -XX:+PrintGCDetails  -XX:+UseParallelGC  -XX:MaxGCPauseMillis=20  -XX:GCTimeRatio=19
     * -ea -Xmx40m -XX:+PrintGCDetails  -XX:+UseParallelGC  -XX:+UseAdaptiveSizePolicy
     *
     *     ���������Ⱥ��������ȡ�
     *  -ea -Xmx40m -XX:+PrintGCDetails  -XX:+UseParallelGC  -XX:+UseAdaptiveSizePolicy -XX:+UseParallelOldGC
     *  -ea -Xmx40m -XX:+PrintGCDetails  -XX:+UseParallelGC  -XX:+UseAdaptiveSizePolicy -XX:+UseParallelOldGC -XX:ParallelGCThreads=100
     *   ==========================cms gc  ������ͣʱ�䣬�����Ӧ������ ���������==================================
     *  -ea -Xmx40m -XX:+PrintGCDetails  -XX:+UseConcMarkSweepGC -XX:ParallelGCThreads=100
     *  -ea -Xmx40m -XX:+PrintGCDetails -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=1 -XX:CMSInitiatingOccupancyFraction=100  -XX:+UseConcMarkSweepGC -XX:ParallelGCThreads=100
     * -ea -Xmx40m -XX:+PrintGCDetails -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=20 -XX:CMSInitiatingOccupancyFraction=100  -XX:+UseConcMarkSweepGC -XX:ParallelGCThreads=640
     *  out-of-memory ÿ��gc�߳̾�̯���ڴ���󣬳����ܵĶ��ڴ棬���±���
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
                    log.error("PigEaster �Ա���...");
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
             * ���߳�ִ�� ++ ������������Ӱ�졣  ��һ����һ��д������Ӱ������
             */
            while (true) {
                Thread.sleep(2000);
                queue_pigs += pigs.size();
                pigs = new CustomArrayList<>();
                if (queue_pigs > pig_nums) {
                    log.info("time interval(/s) : {}, pigs nums:{}",
                            (System.currentTimeMillis() - start) / 1000, queue_pigs);
                    return;
                    // default 5011 - 689627s           <1�� - 138s>
                    // �ܽᣬ��ǰ�����²�಻��
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
