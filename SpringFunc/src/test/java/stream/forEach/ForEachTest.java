package stream.forEach;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ForkJoinPoolFactoryBean;
import org.springframework.stereotype.Component;
import sample.mybatis.mapper.MapperInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;

@Slf4j
public class ForEachTest {
    static CompletableFuture<String> completableFuture = null;

//    @BeforeEach
//    public void setUp() {
//        completableFuture = CompletableFuture.supplyAsync(() -> {
//            return "";
//        });
//    }

    static class ApplyString implements Function<String, String> {
        @Override
        public String apply(String o) {
//            throw new RuntimeException("intend");
            return "i am ...";
        }
    }

    static class ApplyTwoString<U> implements Function<String, CompletionStage<U>> {
        @Override
        public CompletionStage<U> apply(String o) {
            return (CompletionStage<U>) completableFuture.thenApplyAsync(
                    (String ss) -> {
                        return String.format("%s---i am hcj ,i am back !!!", o);
                    });
        }
    }

    static class CustomCallable implements Callable {
        @Override
        public Object call() throws Exception {
            log.info("hello world!!!,currentThread-Name: {}", Thread.currentThread().getName());
            return "123";
        }
    }

    /**
     * state != NEW && state != COMPLETING && state != INTERRUPTING;
     * COMPLETING or INTERRUPTING : false dead  but  does not done
     */
    @Test
    public void bugReproduce() throws ExecutionException, InterruptedException {
        long l = System.nanoTime();
        log.info("{}", l);
        FutureTask futureTask = new FutureTask<String>(new CustomCallable());
        Future<?> submit = Executors.newSingleThreadExecutor().submit(futureTask);
        submit.get();
    }

    @Test
    public void FutureTaskTest2() throws ExecutionException, InterruptedException {

        FutureTask futureTask = new FutureTask<String>(new CustomCallable());
        Future<?> submit = Executors.newSingleThreadExecutor().submit(futureTask);
        Object o = submit.get();
        /**
         *    in the Treiber  stack, you can many times to get(); , likes the observer  model
         *    then when invoke get() ,then state from new to complete and cancel() return false
         *
         */
        futureTask.get();
        futureTask.get();
        futureTask.get();

        boolean cancel = futureTask.cancel(true);
        assert cancel == false;

        o = futureTask.get();
        log.info("{}", o);
    }

    @Test
    public void FutureTaskTest() throws ExecutionException, InterruptedException {
        // what 's location ,. how to use it ! what it is definition?
        FutureTask futureTask = new FutureTask<String>(new CustomCallable());
        futureTask.run();

        Object o = futureTask.get();
        log.info("{}", o);

    }

    private CompletableFuture<String> doAsyncReturnCompletableFuture(final int i) {
        CompletableFuture<String> completableFuture = null;
        try {
            completableFuture = CompletableFuture.supplyAsync(() -> {
                return String.format("%s,param:i:%s", Thread.currentThread().getName(), i);
            });
            throw new RuntimeException("intend");
        } catch (Exception e) {
            if(completableFuture!=null){
                completableFuture.completeExceptionally(e);
            }
        }
        return completableFuture;
    }

    private String doAsync(int i) {

        CompletableFuture<String> voidCompletableFuture = completableFuture.thenApplyAsync((s) -> {
            int i1 = ThreadLocalRandom.current().nextInt(5);

            if (i1 == 1 || i1 == 2) {
                throw new RuntimeException("intend");
            }
            return ("do you speak in English?");
        });
        /**
         *  ����ʽ��չʾ������������ס������ȫ���޷�ִ�С� �������ĺô��� ���Ǻ� spring ������ϣ�����������ã�
         *  spy
         */
        String s = "";
        try {
            s = completableFuture.thenApplyAsync(new ApplyString()).thenCombineAsync(voidCompletableFuture, (k1, k2) -> {
                return String.format("%s,%s,%d", k1, k2, i);
            }).get();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    @Test
    public void StreamParallelVsCompletableFuture() {
        /**
         * StreamParallel: �ʺϼ����ܼ���Ӧ��   �����ù̶����ֳ�������parallerl=4�� �����ڶ��io�ӳٵ����񣬻ᵼ���޷�������ö�cpu�ļ�����Դ
         * CompletableFuture:�ʺ�io�ȴ��ĳ�����  �������������ĳ���ִ�С�
         */
    }

    @Test
    public void asyncFutureWithReturnForCollection() {
        AtomicInteger i = new AtomicInteger();
        List<CompletableFuture<String>> collect = Stream.generate(() -> {
            return i.getAndIncrement();
        }).parallel().limit(100).map(s -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return doAsyncReturnCompletableFuture(s);
        }).collect(Collectors.toList());

        CompletableFuture[] completableFutures = collect.toArray(new CompletableFuture[0]);
        CompletableFuture.anyOf(completableFutures).thenAccept(System.out::println);

        // ��Ƚ�ͬ�����첽���Щ��
        /**
         collect.stream().parallel().map(c -> {
         return c.join();
         }).forEach(System.out::println);
         */



    }


    @Test
    public void asyncFuture() {
        // foreach
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Stream.generate(() -> {
            return atomicInteger.getAndIncrement();
        }).parallel().forEach(
                s -> {
                    String s1 = doAsync(s);
                    log.info("i say: {}", s1);
                }
        );

    }


    @Test
    public void CompletableFutureTest() {


        CompletableFuture<String> voidCompletableFuture = completableFuture.thenApplyAsync((s) -> {
            return ("do you speak in English?");
        });

        // �������� vs listenFuture
        try {
            // thenCombine , ���޲����������񣬽��в�֣�
            String s = completableFuture.thenApplyAsync(new ApplyString()).thenCombine(voidCompletableFuture, (k1, k2) -> {
                return String.format("%s,%s", k1, k2);
            }).get();
            Assert.assertEquals(s, "i am ...,do you speak in English?");
            s = completableFuture.thenApplyAsync(new ApplyString()).thenCombine(voidCompletableFuture, (k1, k2) -> {
                return String.format("%s,%s", k1, k2);
            }).get();
            Assert.assertEquals(s, "i am ...,do you speak in English?");
            s = (String) completableFuture.thenApplyAsync(new ApplyString()).thenCompose(new ApplyTwoString()).get();
            Assert.assertEquals(s, "i am ...---i am hcj ,i am back !!!");
            s = completableFuture.applyToEitherAsync(voidCompletableFuture, (k1) -> {
                return String.format("quick get %s", k1);
            }).get();
            Assert.assertTrue(s.equals("quick get ") || s.equals("quick get do you speak in English?"));
            completableFuture.thenAcceptBothAsync(voidCompletableFuture, (k1, k2) -> {
                Assert.assertTrue(k1.equals("") || k2.equals("do you speak in English?"));
            });
            completableFuture.whenCompleteAsync((k1, k2) -> {
                if (k2 == null && k2.getCause() == null) {
                } else {
                    log.info("no error , resut: {} ", k1);
                }
            });

            log.info("result: {}", s);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


//        completableFuture.thenApply()
//        completableFuture.runAfterBoth()
//        completableFuture.thenApply().thenAccept().thenCombine();


    }

    @Test
    public void ExecutorServiceTest() {
        /**
         *     private static final int RUNNING    = -1 << COUNT_BITS;
         *     private static final int SHUTDOWN   =  0 << COUNT_BITS;
         *     private static final int STOP       =  1 << COUNT_BITS;
         *     private static final int TIDYING    =  2 << COUNT_BITS;
         *     private static final int TERMINATED =  3 << COUNT_BITS;
         */
        int a = -1 << 32; // -1
        int a1 = 0 << 32; // 0
        int a2 = 2 << 32; // 2

        // 2 ^ (12 *2 + 6)

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<?> hello_wolrd = executorService.submit(() -> {
            log.info("hello wolrd");
        });

    }

    @Test
    public void parallelismTest() {
        /**
         * c = -4294967296L
         * 64   & (48  -4294967296L)   |  48 + 32 -4294967296L
         */


        long AC_SHIFT = 48;
        long AC_MASK = 0xffffL << AC_SHIFT;
        long TC_SHIFT = 32;
        long TC_MASK = 0xffffL << TC_SHIFT;
        long ctl = 0;
        long AC_UNIT = 48;
        long TC_UNIT = 32;
        long parallelism = 2;
        long c = -4294967296L;

        long nc = ((AC_MASK & (c + AC_UNIT)) |
                (TC_MASK & (c + TC_UNIT)));

        long np = (long) (-parallelism); // offset ctl counts
        // -1 << 0xffff << 48    -1
        // -1 << 0xffff << 32
        // -1 << 48   63 - 48  = 15  -
        //  1111  1111 1111 1110 - > 64-48
        //  1111  1111 1111 1110 - > 64-48
        ctl = ((np << AC_SHIFT) & AC_MASK) | ((np << TC_SHIFT) & TC_MASK);
        log.info("{}", ctl);
        /**
         *  when parallelism equal 1 then  :  -4294967296
         *  when parallelism equal 2  then  : -281483566645248
         */
    }

    static class CustomForkJoinTask extends ForkJoinTask {

        @Override
        public Object getRawResult() {
            return null;
        }

        @Override
        protected void setRawResult(Object value) {
        }

        @Override
        protected boolean exec() {
            throw new RuntimeException("intend");
        }
    }

    @Test
    public void commonPoolTest() throws InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool(1);

        ForkJoinTask submit = forkJoinPool.submit(new CustomForkJoinTask());
        Thread.sleep(1000 * 3);
        Throwable exception = submit.getException();
//        log.info("{}", exception.getStackTrace());


        int SMASK = 0xffff;
        System.out.println(SMASK); // 65535
        Long maxValue = Long.valueOf(Integer.MAX_VALUE);
        Long a = Long.valueOf(Integer.MAX_VALUE);
        // 1111 1111 1111 1111
        // 0,f -> 0, 15
        // 0 -7 , a -f
        String binaryBit = "9f";
        int i = Integer.parseInt(binaryBit, 16);
        int mode = (1 << 16 | 4) & 0xffff << 16;

        ForkJoinPoolFactoryBean forkJoinPoolFactoryBean = new ForkJoinPoolFactoryBean();
        forkJoinPoolFactoryBean.getObject();

    }

    @Test
    public void StreamTest() {
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<String, String>() {
            {
                put("1", "2");
                put("2", "3");
                put("11122", "2");
                put("21", "3");
                put("212", "2");
                put("2121", "3");
                put("11", "2");
                put("221", "3");
                put("5", "2");
                put("6", "3");
            }
        };
        concurrentHashMap.keySet().stream().parallel().forEach(s -> {
            log.info("{}", s.toString());
        });
        MapperInterface mock = mock(MapperInterface.class);
        concurrentHashMap.entrySet().stream().forEach(CustomConsumer::accept);
        CopyOnWriteArrayList<Object> objects = new CopyOnWriteArrayList<>();
        objects.stream().parallel().forEach(s -> {
            s.toString();
        });
        objects.stream().parallel().map(String::valueOf).collect(Collectors.toList());

    }


    @Component
    static class CustomConsumer {
        @Autowired
        private MapperInterface mapperInterface1;

        public static void accept(Map.Entry<String, String> map) {
            log.info("{}", map);
        }
    }

    @Test
    public void listStreamTest() {

        AtomicInteger atomicInteger = new AtomicInteger(0);

        ArrayList<Integer> integers = Lists.newArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        integers.stream().forEach(s -> {
            atomicInteger.addAndGet(s);
        });
        log.info("{}", atomicInteger.get());
        assert atomicInteger.get() == 55;
        atomicInteger.set(0);
        integers.stream().parallel().forEach(s -> {
            atomicInteger.addAndGet(s);
        });
        // 0 +1
        log.info("{}", atomicInteger.get());
        assert atomicInteger.get() == 55;

    }

    @SneakyThrows
    @Test
    public void asyncTest() throws ExecutionException, InterruptedException {
        // todo common pool �޷�ֹͣ��
        // sample use:  how fork combine with fork1  in the PacketSender
        PacketSender p = new PacketSender();
        ForkJoinTask<Integer> fork = new HeaderBuilder(p).fork();
//        ForkJoinTask<Integer> fork1 = new BodyBuilder(p).fork();
        Integer join = fork.join();
//        Integer integer = fork.invoke();
//        Integer integer1 = fork1.invoke();

        log.info("{},{}", 1, join);

    }

    @Test
    public void mapReduceTwoTest() {
        MyMapper<Integer> integerMyMapper = new MyMapper<>();
        MyReducer<Integer> integerMyReducer = new MyReducer<>();
        // 1 + 2 + ... + 10
        AtomicInteger x = new AtomicInteger(0);
        List<Integer> collect = Stream.generate(() -> {
            x.set(x.get() + 1);
            return x.get();
        }).limit(10).collect(Collectors.toList());
        Integer res = MapReducerTwo.mapReduce(collect.toArray(new Integer[0]), integerMyMapper, integerMyReducer);
        log.info("the result of mapReducer{}", res);
    }

    @Test
    public void mapReduceTest() {
        MyMapper<Integer> integerMyMapper = new MyMapper<>();
        MyReducer<Integer> integerMyReducer = new MyReducer<>();
        // 1 + 2 + ... + 10
        AtomicInteger x = new AtomicInteger(0);
        List<Integer> collect = Stream.generate(() -> {
            x.set(x.get() + 1);
            return x.get();
        }).limit(10).collect(Collectors.toList());
        Integer res = MapReducer.mapReduce(collect.toArray(new Integer[0]), integerMyMapper, integerMyReducer);
        log.info("the result of mapReducer{}", res);
    }

    @Test
    public void searchTest() throws ExecutionException, InterruptedException {
        AtomicInteger x = new AtomicInteger(0);
        List<String> collect = Stream.generate(() -> {
            x.set(x.get() + 1);
            return x;
        }).limit(10).map(String::valueOf).collect(Collectors.toList());
        String search1 = Searcher.search(collect.toArray(new String[0]));
        String search2 = Searcher.searchItem(collect.toArray(new String[0]));
        log.info("conguration , got it!!! {},{}", search1, search2);
    }

    @Test
    public void rightMoveTest() {
        //
        int maxInteger = 0x7fffffff;
        System.out.println((Math.pow(2, 31)) - 1);
        //

        int a = 1;
        a = a >>> 1; // 0
        // 0111 1111   2^32
        //
        int b = -1; //
        b = b >>> 1; // not -1 but 2147483647   sign ҲҪ����
        // ����λ ҲҪռһλ  0 31��1
//        Integer.MAX_VALUE   2^31 -1 int ����

        // 1111
        // 1111
        int c = -1;
        c = c >> 1; // not 1  but -1  sign ��Ҫ����

        log.info("a:{},b:{},c:{}", a, b, c);
    }

    @Test
    public void test4() {
        MyOperation myOperation = new MyOperation();
        AtomicInteger x = new AtomicInteger(0);
        List<String> collect = Stream.generate(() -> {
            x.set(x.get() + 1);
            return x;
        }).limit(10).map(String::valueOf).collect(Collectors.toList());
        ForEachVersionFour.<String>forEach(collect.toArray(new String[0]), myOperation);
    }

    @Test
    public void test3() {
        MyOperation myOperation = new MyOperation();
        AtomicInteger x = new AtomicInteger(0);
        List<String> collect = Stream.generate(() -> {
            x.set(x.get() + 1);
            return x;
        }).limit(10).map(String::valueOf).collect(Collectors.toList());
        ForEachVersionThere.<String>forEach(collect.toArray(new String[0]), myOperation);
    }

    @Test
    public void test2() {
        MyOperation myOperation = new MyOperation();
        AtomicInteger x = new AtomicInteger(0);
        List<String> collect = Stream.generate(() -> {
            x.set(x.get() + 1);
            return x;
        }).limit(10).map(String::valueOf).collect(Collectors.toList());
        ForEachVersionTwo.<String>forEach(collect.toArray(new String[0]), myOperation);
    }

    @Test
    public void test1() {
        MyOperation myOperation = new MyOperation();
        AtomicInteger x = new AtomicInteger(0);
        List<String> collect = Stream.generate(() -> {
            x.set(x.get() + 1);
            return x;
        }).limit(10).map(String::valueOf).collect(Collectors.toList());
        ForEach.<String>forEach(collect.toArray(new String[0]), myOperation);
    }
}
