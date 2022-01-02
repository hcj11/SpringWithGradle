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
        // 不存在依赖关系，可以多次异步，执行，不必都卡在某一步 ， 提供completeFuture 即可。
        // 采用jdk1.8的方式。
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
         *    多线程采用 同步机制进行，work的同步操作。         *
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
         *  采用cas ,自旋的方式，进行等待。处理。
         *  会导致pool故障， 不会导致jvm故障，  onStart();
         *  pool运行不会导致pool故障，
         *  parallel() is true:  同样可以采用spliterator 框架，实现并行的执行任务
         *  parallel() is false: 同样可以采用spliterator 框架，实现顺序的执行任务
         *  .parallel().limit(50) 无法采用并行任务，
         *  */
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = (Thread t, Throwable e) -> {
            log.error("thread: {},error message: {}", t.getName(), e.getMessage());
        };
        CustomForkJoinWorkerThreadFactory customForkJoinWorkerThreadFactory = new CustomForkJoinWorkerThreadFactory();

        ForkJoinPool forkJoinPool =
                new ForkJoinPool(10,
                        customForkJoinWorkerThreadFactory, uncaughtExceptionHandler, false);


// 源头 - 中继
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
