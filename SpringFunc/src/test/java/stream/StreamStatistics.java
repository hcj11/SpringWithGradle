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
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.function.Consumer;
import java.util.stream.Collectors;
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


        int j = (1 + 4) >>> 1; //
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
    public void test3() throws IOException {
        // assert  don't throws heap space
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Stream.generate(()->{return atomicInteger.getAndIncrement();}).forEach((s)->{
            if(s % 1000000==0){
                log.info("1000000 ���� :{}",s);
            }
        });// 1609000000
    }
    @Test
    public void test1() throws IOException {
        ArrayList<Object> objects = Lists.newArrayList(1,2,3,4,5,5,6,7,7,8,8,8);
        objects.stream().parallel().forEach(System.out::println);
        Stream.generate(Math::random).limit(5).forEach(System.out::println);
        Stream.iterate(2, integer -> integer * 2);
        Files.lines(Paths.get(""));
    }

    @Test
    public void test4(){

// so distinct(linkedHashSet) vs new HashSet 's distinct method
        log.info("=new HashSet=start==");
        ArrayList<String> strings = Lists.newArrayList("3", "2", "1", "3", "4", "2");
        HashSet<String> strings1 = new HashSet<>(strings);
        Assert.assertEquals(strings1.toArray(new String[0]),new String[]{"1","2","3","4"});

        log.info("=new distinct=start==");
        List<String> collect = Lists.newArrayList("3", "2", "1", "3", "4", "2").stream().distinct().collect(Collectors.toList());
        Assert.assertEquals(collect.toArray(),new String[]{"3","2","1","4"});

        LinkedHashSet linkedHashSet = new LinkedHashSet();
        linkedHashSet.add("3");
        linkedHashSet.add("2");
        linkedHashSet.add("1");
        linkedHashSet.add("3");
        LinkedHashSet linkedHashSet1 = new LinkedHashSet();
        linkedHashSet1.add("4");
        linkedHashSet1.add("2");

        boolean b = linkedHashSet.addAll(linkedHashSet1);

        Assert.assertEquals(linkedHashSet.toArray(new String[]{}),new String[]{"3","2","1","4"});
    }




}
