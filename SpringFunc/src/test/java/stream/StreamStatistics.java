package stream;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Spliterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Slf4j
public class StreamStatistics {

    @Test
    public void nextGaussian() {
        Executors.newSingleThreadExecutor();

        double v = ThreadLocalRandom.current().nextGaussian();
        System.out.println(v);

    }

    static <T> void parEach(TaggedArray<T> a, Consumer<T> action) {

        Spliterator<T> s = a.spliterator();
        long targetBatchSize = s.estimateSize() / (ForkJoinPool.getCommonPoolParallelism() * 8);
        new TaggedArray.ParEach(null, s, action, targetBatchSize).invoke();
    }
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Data
//    static class CustomInteger extends java.lang.Integer {
//
//        public CustomInteger(int value) {
//            super(value);
//        }
//    }

    @Test
    public void atomicIntegerUpdate() {
        // todo look up sources
        // Caused by: java.lang.IllegalAccessException at StreamStatistics.java:53
        AtomicIntegerFieldUpdater<Integer> counter = AtomicIntegerFieldUpdater.newUpdater(Integer.class, "value");
        int i = counter.addAndGet(new Integer(1), 1);
        Assert.assertEquals(i, 2);
    }

    @Test
    public void CountedCompleter() {

        // 16 ABASE , 2 ASHIFT
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.submit(() -> {
            log.info("");
        });

    }

    @Test
    public void taggedArrayTest() {
        // T[] data, Object[] tags
        String[] strings = {"2", "3", "4", "5"};
        Object[] tags = {1, 2, 3, 4};
        TaggedArray<String> stringTaggedArray = new TaggedArray<>(strings, tags);
        parEach(stringTaggedArray, t -> {
            log.info("{}", t);
        });
    }

    @Test
    public void test2() {
        int iright = 12 << 0;
        int ileft = 12 >> 0;

        long negative = 0xf0000000;
        String s = Long.toString(negative, 16);
//        long l = Long.parseLong(String.valueOf(negative), 16);
//        Long.toHexString(negative)


        int j = (1 + 4) >>> 1; // 向下取整
        //  0101
        //  0010 =2  0010
        // 1-9 10 -17
        // 1-8 9-17
        //  8   9
        //
        // 0001   1000 & 1110  1000 =  8
        //  8 & ~1  ~(~1) =1
        // 12 / 2 =6
        //  0110 & 1110
        //  1110
        //  0110  2 + 4 = 6
        // 10100  = 2  + 16 = 18
        // 0110
        // 1110
        // 1111110 = -2
        // 2
        int a = ~(~1);
        Assert.assertTrue(a == 1);
        int i = 12 >>> 1 & ~1;
        int ii = 12 >>> 1 & -1;
        log.info("{}", i);
    }

    @Test
    public void test1() throws IOException {
        ArrayList<Object> objects = Lists.newArrayList(1,2,3,4,5,5,6,7,7,8,8,8);

        objects.stream().parallel().forEach(System.out::println);
//        objects.stream().spliterator().forEachRemaining(System.out::println);

//        objects.stream().forEach();
        Stream.generate(Math::random).limit(5).forEach(System.out::println);
        Stream.iterate(2, integer -> integer * 2);
//        Files.newBufferedReader();
        Files.lines(Paths.get(""));

    }


}
