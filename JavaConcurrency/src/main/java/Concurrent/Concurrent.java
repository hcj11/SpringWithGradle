package Concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 设定一个丢弃策略
 */
class A {
    private String name;
    private String value;
}

@Slf4j
class CustomThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable r) {
        System.out.println();
        Thread thread = new Thread(r);
        thread.setUncaughtExceptionHandler((Thread t, Throwable e) -> {
            log.error("{}", e.getMessage());
        });
        return thread;
    }
}
@Slf4j
public class Concurrent {
    public static ThreadPoolExecutor make(int nThreads, RejectedExecutionHandler rejectedExecutionHandler) {
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(3),// 最多3个
                threadFactory,rejectedExecutionHandler);
    }

    public static void abortMake(int nThreads) {
        normal(make(1, new ThreadPoolExecutor.AbortPolicy()));
    }

    public static void discardMake(int nThreads) {
        normal(make(1, new ThreadPoolExecutor.DiscardPolicy()));
    }

    public static void discardOldestMake(int nThreads) {
        normal(make(1, new ThreadPoolExecutor.DiscardOldestPolicy()));
    }

    public static void callRunsMake(int nThreads) {
        normal(make(1, new ThreadPoolExecutor.CallerRunsPolicy()));
    }

    public static void normal(ThreadPoolExecutor make) {
        int i = 0;
        while (i++ < 5) {
//            log.info("当前线程池状态: {}",make);
            int finalI = i;
            make.submit(() -> {
                log.info("{},{}",Thread.currentThread().getName(), finalI);;
//                try {
//                    Thread.sleep(10000000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            });
        }
        make.shutdown();
    }

    public static void main(String[] args) {
        // 1个工作线程。+ 3个任务大小。之后第5个会报错
        // Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task java.util.concurrent.FutureTask@1d56ce6a rejected from java.util.concurrent.ThreadPoolExecutor@5197848c[Running, pool size = 1, active threads = 1, queued tasks = 3, completed tasks = 0]
//         abortMake(1);
//        discardMake(1); // 丢弃最新传递过来的
//        discardOldestMake(1); // 丢弃最老。
//        callRunsMake(1);

    }
}
