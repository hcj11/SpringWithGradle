package stream.concurrency;

import com.google.common.util.concurrent.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class CustomConcurrency {
    class Custom extends AbstractFuture<String> {
        @Override
        protected boolean set(@Nullable String value) {
            return super.set(value);
        }
    }

    //    @Test
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    static class Explosion {
        private String type;
    }

    private Explosion pushBigRedButton() {
        //

        throw new RuntimeException("intend");
//        return  new Explosion("1");
    }

    private void walkAwayFrom(Explosion explosion) {
        log.info("walkAwayFrom type: {}", explosion.getType());
    }

    private void battleArchNemesis(Throwable thrown) {
        log.info("error msg : {}", thrown.getMessage());
    }

    static ListeningExecutorService listeningExecutorService = null;

    public volatile Object lock = new Object();
    int TASKS_PER_PHASER = 3;

    @BeforeEach
    public void setUp() {
        listeningExecutorService = MoreExecutors.newDirectExecutorService();
    }


    private ListenableFuture<String> dataQueryService(String input) {
        ListenableFuture<String> submit = listeningExecutorService.submit(() -> {
            Thread.sleep(4000);
            log.info("---submit 1 ");
            throw new RuntimeException("intend");
//            return "hello world1";
        });
        return submit;
    }

    /**
     *
     */
    @Test
    public void test1() {

    }

    /**
     * 常 用在 client  example  iphone or android
     */
    @Test
    public void singleListenableFutureWithMultiAsynchronousOperations() throws ExecutionException, InterruptedException {


        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        ExecutorService exitingExecutorService = MoreExecutors.getExitingExecutorService(threadPoolExecutor);


        ListenableFuture<String> submit2 = listeningExecutorService.submit(() -> {
            log.info("---submit 2 ");
            return "hello world";
//            throw new RuntimeException("intend");
        });

        AsyncFunction<String, String> asyncFunction = new AsyncFunction<String, String>() {
            @Override
            public ListenableFuture<String> apply(@Nullable String input) throws Exception {
                Thread.sleep(4000);
                return dataQueryService(input);
            }
        };

        // figure out  all error msg from outer and inner future.
        ListenableFuture<String> stringListenableFuture = Futures.transformAsync(submit2, asyncFunction, exitingExecutorService);
        log.info("----{}", stringListenableFuture.get());
        ;
//        Assert.assertEquals(stringListenableFuture.get(),"hello world1");

        log.info("-----start--listListenableFuture--");
        // throws runtimeException('intend')
        Futures.allAsList(submit2, dataQueryService("input..."));
        ListenableFuture<List<String>> listListenableFuture = Futures.allAsList(submit2, dataQueryService("input..."));
        List<String> strings = listListenableFuture.get();
        strings.stream().forEach(System.out::println);
        log.info("-----end---listListenableFuture-");

    }

    @Test
    public void ListenableFutureTest() {
        ListeningExecutorService listeningExecutorService = MoreExecutors.newDirectExecutorService();
        listeningExecutorService.submit(() -> {
        });


        // fan-out
        // fan-in
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<Explosion> explosion = service.submit(
                new Callable<Explosion>() {
                    public Explosion call() {
                        return pushBigRedButton();
                    }
                });
        Futures.addCallback(
                explosion,
                new FutureCallback<Explosion>() {
                    // we want this handler to run immediately after we push the big red button!
                    public void onSuccess(Explosion explosion) {
                        walkAwayFrom(explosion);
                    }

                    public void onFailure(Throwable thrown) {
                        battleArchNemesis(thrown); // escaped the explosion!
                    }
                },
                service);


    }

    @Test
    public void ModTest() {
        int f = 1 >>> 16;
        Assert.assertTrue(f == 0);
        int e = -2;
        e >>= 1;

        int a = Integer.MAX_VALUE; //  2^31 -1 >>>31
        a >>>= 30;
        Assert.assertTrue(a == 1);
        int a1 = Integer.MAX_VALUE;

        int b = -1;
        b >>>= 31;
        Assert.assertTrue(b == 1);
        /**
         * -1 >> 1  向左补 1
         * 1  >> 1  向左补 0
         */
        // -1 >>=55(anything)   = -1
        int c = -1;
        c >>= 55;
        Assert.assertTrue(c == -1);

        int c1 = -1;
        c1 <<= 2;
        log.info("c1 <<=2 =>{}", c1);

        int c2 = 1;
        c2 <<= 2;
        Assert.assertTrue(c2 == 4);
        log.info("c2 <<=2 =>{}", c2);

        int d = 0x80000000;
        log.info("{}", d);
    }

    @Test
    public void QueueTestOtherOfferAndOtherTakeInOrdered() throws InterruptedException {
        SynchronousQueue<String> strings2 = new SynchronousQueue<>(false);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100);
                    String s = strings2.take();
                    log.info("{}", s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            final int[] init = {1};
            Stream.generate(() -> {
                return init[0] <<= 1;
            }).limit(4).forEach(s -> {
                try {
                    strings2.put(String.valueOf(s));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }).start();
        ;

        countDownLatch.await();

    }

    /**
     * Exchanger: 模型， 线程队列，
     * 竞争决策： arenaExchange
     * 超时：timeout
     */
    @Test
    public void exchangeTest() {

        Exchanger<String> exchanger = new Exchanger<>();
        int i = 5; // 6
        while ((i--) != 0) {
            new Thread(() -> {
                try {
                    String hhelloworld2 = exchanger.exchange("hhelloworld2");
                    log.info("hhelloworld2->hhelloworld {}", hhelloworld2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        int j = 6; // 6
        while ((j--) != 0) {
            new Thread(() -> {
                try {
                    String hhelloworld = exchanger.exchange("hhelloworld");
                    log.info("hhelloworld->hhelloworld2 {}", hhelloworld);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 多次offer，多次take,  has no internal capacity ,put > take  put 等待 take . 无队列，故剩下的线程需等待。
     */
    @Test
    public void QueueTestMultiMainPutAndMultiOtherTakeInOrder() throws InterruptedException {
        SynchronousQueue<String> strings2 = new SynchronousQueue<>(true);
        int i = 5; // 6
        while ((i--) != 0) {
            new Thread(() -> {
                try {
                    log.info("take bean:{}", strings2.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        ArrayList<Thread> threads = Lists.<Thread>newArrayList();
        int j = 6; // 6
        while ((j--) != 0) {
            threads.add(new Thread(() -> {
                int i1 = ThreadLocalRandom.current().nextInt(100);
                try {
                    log.info("put bean:{}", i1 + "");
                    strings2.put(i1 + "");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        }
        threads.parallelStream().forEach(t->{t.start();});
        while (true){
            Thread.sleep(2000);
            threads.stream().
                    filter(t->{return t.getState()!= Thread.State.TERMINATED;}).forEach(t->{
                        log.info("not TERMINATED thread state:{},name:{}",t.getState().name(),t.getName());
            });
        }

    }

    /**
     * importance:
     * the pair of  take and put  infinite time wait otherwise
     * the pair of  poll and offer  finite time wait
     */
    @Test
    public void QueueTestMainPutAndOtherTakeInOrder() throws InterruptedException {
        SynchronousQueue<String> strings2 = new SynchronousQueue<>(true);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(() -> {
            while (true) {
                try {
                    // take it easy .
                    Thread.sleep(100);
                    String s = strings2.take();
                    log.info("{}", s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Stream.generate(Math::random).limit(3).forEach(s -> {
            try {
                strings2.put(String.valueOf(s));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        countDownLatch.await();
    }

    /**
     * feature:  offer 、poll 带有超时机制，存在错过 （存取）元素的情况，
     */
    @Test
    public void QueueTest() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(5);
        /**
         * SynchronousQueue supply timeout manipulate ，
         */
        SynchronousQueue<String> strings2 = new SynchronousQueue<>(true);
        int length = 5;
        for (int i = 0; i < length; i++) {
            int finalI = i;
            new Thread(() -> {
                log.info("----offer start");
                try {
                    strings2.offer(finalI + "", 3, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("----offer end");
                countDownLatch.countDown();
            }).start();

        }
        log.info("----await start");
        countDownLatch.await();

        log.info("----await end");
        String res = "";
        while ((res = strings2.poll()) != null) {
            log.info("{}", res);
            res = null;
        }

    }

    void startTasks(List<Runnable> tasks, final int iterations) throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        final Phaser phaser = new Phaser(1) {
            protected boolean onAdvance(int phase, int registeredParties) {
//                if (UNSAFE.compareAndSwapLong(this, stateOffset, s,
//                        s -= ONE_ARRIVAL)) {
                // s-1 -> <0 停止，count
                log.info("{}", atomicInteger.incrementAndGet());
                return registeredParties == 0;
            }
        };
        phaser.register();
        for (final Runnable task : tasks) {
            phaser.register(); // +1
            new Thread() {
                public void run() {
                    do {
                        task.run();
                        phaser.arriveAndAwaitAdvance();
                    } while (!phaser.isTerminated());
                }
            }.start();
        }
        phaser.arriveAndDeregister(); // deregister self, don't wait
        phaser.arriveAndDeregister();
        synchronized (lock) {
            lock.wait();
        }

    }

    void awaitPhase(int phase) {
        Phaser phaser = new Phaser(); // parties = 0 ,state =1 ;
        int p = phaser.register(); // assumes caller not already registered
        while (p < phase) {
            if (phaser.isTerminated()) {
                // for example:   if (UNSAFE.compareAndSwapLong(this, stateOffset, s,s -= ONE_ARRIVAL)) {
                log.info("unexpected Terminated {},initPhase:{}", p, phase);
            } else
                p = phaser.arriveAndAwaitAdvance();
        }
        Assert.assertEquals(p, 10);

        int i = phaser.arriveAndDeregister();

    }

    @NoArgsConstructor
    @Data
    class Task implements Runnable {

        Phaser phaser;

        public Task(Phaser phaserNew, CountDownLatch countDownLatch) {

            phaser = phaserNew;
            if (phaserNew != null) {

                if (phaserNew.getParent() != null) {
                    int registeredParties = phaserNew.getParent().getRegisteredParties();
                    log.info("current phaserNew:{},the second registeredParties: {}", phaserNew, registeredParties);
                }
                phaserNew.register();
                countDownLatch.countDown();
            }
        }

        @Override
        public void run() {
            int i = ThreadLocalRandom.current().nextInt(50);
            log.info("phase:{},{}", phaser.getPhase(), i);
            try {
                Thread.sleep(i * 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            phaser.arriveAndAwaitAdvance();
        }
    }

    void build(Task[] tasks, int lo, int hi, Phaser ph, CountDownLatch countDownLatch) {
        if (hi - lo > TASKS_PER_PHASER) {
            for (int i = lo; i < hi; i += TASKS_PER_PHASER) {
                int j = Math.min(i + TASKS_PER_PHASER, hi);
                build(tasks, i, j, new Phaser(ph), countDownLatch);
            }
        } else {
            /** parent pharser = initial
             * 0,3  parent phaser  0,1,2  new Phaser(ph)
             * 3,4  child phaser   3,     new Phaser(ph)
             */
            for (int i = lo; i < hi; ++i)
                tasks[i] = new Task(ph, countDownLatch);
            // assumes new Task(ph) performs ph.register()
        }
    }

    //  Phaser ,  SynchronousQueue , ListenableFuture
    void runTasks(List<Runnable> tasks) throws InterruptedException {
        final Phaser phaser = new Phaser(1); // "1" to register self        parties =1 -》 直接释放掉
        // create and start threads
        for (final Runnable task : tasks) {
            int register = phaser.register();
            new Thread() {
                public void run() {
                    task.run();
                    int i = phaser.arriveAndAwaitAdvance();// await all creation
                }
            }.start();
        }
        // allow threads to start and deregister self
        int i = phaser.arriveAndDeregister();
        log.info("{}", i);
//        synchronized (lock){
//            lock.wait();
//        }
    }

    @Test
    public void ParentAndChildOfPharse() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(4);

        Phaser phaser = new Phaser(0);
        phaser.register();
        Task[] initTasks = {null, null, null, null};
        build(initTasks, 0, 4, phaser, countDownLatch);
        countDownLatch.await();

        // register and then bootstrap
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        Arrays.stream(initTasks).parallel().forEach(s -> {
            forkJoinPool.submit(s);
        });

        phaser.arriveAndDeregister();

        synchronized (lock) {
            lock.wait();
        }


    }

    @Test
    public void PhaserTest1() throws InterruptedException {
        Runnable runnable = () -> {
            log.info("hello world");
        };
        ArrayList<Runnable> runnables = Lists.newArrayList(runnable);
//        runTasks(runnables);
        startTasks(runnables, 10); // 0-11 => run times is  12
//        awaitPhase(10);

    }

    private synchronized void processRow(int row, Solver solver) throws InterruptedException {
        if (solver.getSpy().getCount() == row) {
            throw new InterruptedException();
//            throw new RuntimeException("intend");
        }

        float[][] data = solver.getData();
        // 1 + 1 = 1  , 1 + 1 =2
        data[row] = new float[]{row + 1, 3, 4, 5};
        log.info("start process the row,{}", row);
    }

    private void mergeRows(Solver solver) {
        int sum = Arrays.stream(solver.getData()).mapToInt(s -> (int) s[0]).sum();
        log.info("start mergeRows -- sum:{}", sum);
//        Assert.assertTrue(sum==3);
    }

    @Data
    class Spy {
        int count = 1;
    }

    @Data
    class Solver {
        final int N;
        final float[][] data;
        final CyclicBarrier barrier;
        Spy spy;

        public Solver(float[][] matrix, Spy spyNew) throws InterruptedException {
            spy = spyNew;
            data = matrix;
            N = matrix.length;
            Runnable barrierAction =
                    new Runnable() {
                        public void run() {
                            mergeRows(Solver.this);
                        }
                    };
            barrier = new CyclicBarrier(N, barrierAction);

            List<Thread> threads = new ArrayList<Thread>(N);
            for (int i = 0; i < N; i++) {
                Thread thread = new Thread(new Worker(i));
                threads.add(thread);
                thread.start();
            }

            // wait until done
            for (Thread thread : threads)
                thread.join();
        }

        class Worker implements Runnable {
            int myRow;

            Worker(int row) {
                myRow = row;
            }

            public void run() {

                try {
                    processRow(myRow, Solver.this);
                    int await = barrier.await();
                    log.info("untripped count,{}", await);
                } catch (InterruptedException ex) {
                    int await = 0;
                    try {
                        await = barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    log.info("untripped count,{}", await);
                    return;
                } catch (BrokenBarrierException ex) {
                    return;
                }
            }
        }
    }


    @Test
    public void CyclicBarrierTest() throws InterruptedException, BrokenBarrierException {
        float[][] matrix = {{1, 2, 3, 4}, {1, 2, 3, 4}};

        Spy mock = mock(Spy.class);
        when(mock.getCount()).thenReturn(1);

        Solver solver = new Solver(matrix, mock);

    }


}
