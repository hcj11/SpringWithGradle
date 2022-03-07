package stream.concurrency;

import ch.qos.logback.core.hook.DelayingShutdownHook;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.startup.SafeForkJoinWorkerThreadFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.concurrent.ForkJoinPool.defaultForkJoinWorkerThreadFactory;

@Slf4j
public class ThreadPoolTest {
    Object lock = new Object();

    class CallableTask implements Callable {

        @SneakyThrows
        @Override
        public Object call() throws Exception {
            log.info("{},hello world ", Thread.currentThread().getName());
             throw new RuntimeException("intend");
//            synchronized (lock) {
////                Thread.sleep(1000);
//                lock.wait();
//            }
//            return null;
        }
    }

    class Task implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            log.info("{},hello world ", Thread.currentThread().getName());
            synchronized (lock) {
                lock.wait();
            }
        }
    }

    @Test
    public void test2() {
        // loop future.get(), have problem , timout
        CompletableFuture.allOf();
//        CompletableFuture.supplyAsync();
        CompletableFuture.completedFuture("");

    }

    @Test
    public void test3() {
        ArrayList<Object> objects = null;
        Optional.ofNullable(objects).ifPresent(s -> {
            List<Object> collect1 = s.stream().distinct().collect(Collectors.toList());
            System.out.println(collect1);
        });

    }

    /**
     * Executors.newSinglePool();
     */
    @Test
    public void errorHandlerForPool() {
        /**
         * core->max->queue
         *    w.lock(); {}  w.unlock();
         *    ���̲߳��� ͬ�����ƽ��У�work��ͬ��������         *
         */
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1));

        RejectedExecutionHandler rejectedExecutionHandler = (Runnable r, ThreadPoolExecutor executor)->{
                log.info("currently, pool:{} can't push the coming task:{},",executor.toString(),r);
       };
        threadPoolExecutor.setRejectedExecutionHandler(rejectedExecutionHandler);


        AtomicInteger s = new AtomicInteger();
        Stream.generate(() -> {
            return s.getAndIncrement();
        }).parallel().forEach(i -> {
            threadPoolExecutor.submit(() -> {
                if(i%5==0){
                    throw new RuntimeException("intend");
                }
            });

        });


    }
    static final class CustomForkJoinWorkerThreadFactory
            implements ForkJoinPool.ForkJoinWorkerThreadFactory {
        public final ForkJoinWorkerThread newThread(ForkJoinPool pool) {
            CustomForkJoinWorkerThread customForkJoinWorkerThread = new CustomForkJoinWorkerThread(pool);
            customForkJoinWorkerThread.setDaemon(true);
            return customForkJoinWorkerThread;
        }
    }

    @Slf4j
    public static class CustomForkJoinWorkerThread extends ForkJoinWorkerThread{
        /**
         * Creates a ForkJoinWorkerThread operating in the given pool.
         *
         * @param pool the pool this thread works in
         * @throws NullPointerException if pool is null
         */
        protected CustomForkJoinWorkerThread(ForkJoinPool pool) {
            super(pool);
        }
        @Override
        protected void onStart() {
            super.onStart();
//            throw new RuntimeException("CustomForkJoinWorkerThread-intend");
        }
    }

    @Test
    public void test4(){

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CustomForkJoinWorkerThread customForkJoinWorkerThread = new CustomForkJoinWorkerThread(forkJoinPool);
        forkJoinPool.submit(()->{});
    }
    @Test
    public void test1() throws InterruptedException {
        /**
         *
         *  ����cas ,�����ķ�ʽ�����еȴ�������
         *  �ᵼ��pool���ϣ� ���ᵼ��jvm���ϣ�  onStart();
         *  pool���в��ᵼ��pool���ϣ�
         *  parallel() is true:  ͬ�����Բ���spliterator ��ܣ�ʵ�ֲ��е�ִ������
         *  parallel() is false: ͬ�����Բ���spliterator ��ܣ�ʵ��˳���ִ������
         *  .parallel().limit(50) �޷����ò�������
         *  */
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = (Thread t, Throwable e) -> {
            log.error("thread: {},error message: {}", t.getName(), e.getMessage());
        };
        CustomForkJoinWorkerThreadFactory customForkJoinWorkerThreadFactory = new CustomForkJoinWorkerThreadFactory();

        ForkJoinPool forkJoinPool =
                new ForkJoinPool(10,
                        customForkJoinWorkerThreadFactory, uncaughtExceptionHandler, false);


// Դͷ - �м�
        AtomicInteger s = new AtomicInteger();
        Stream.generate(() -> {
            return s.getAndIncrement();
        }).parallel().limit(50).forEach(i -> {
            log.info("currentThread:{}",Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            ForkJoinTask submit = forkJoinPool.submit(new CallableTask());
        });
        synchronized (lock) {
            lock.wait();
        }


    }
}
