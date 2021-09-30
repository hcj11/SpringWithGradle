package stream.forEach;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ForEachTest {

    @SneakyThrows
    @Test
    public void asyncTest() throws ExecutionException, InterruptedException {

        // sample use:  how fork combine with fork1  in the PacketSender
        PacketSender p = new PacketSender();
        ForkJoinTask<Integer> fork = new HeaderBuilder(p).fork();
        ForkJoinTask<Integer> fork1 = new BodyBuilder(p).fork();

        Integer integer = fork.get();
        Integer integer1 = fork1.invoke();

        log.info("{},{}",integer1,integer);

    }

    @Test
    public void mapReduceTwoTest(){
        MyMapper<Integer> integerMyMapper = new MyMapper<>();
        MyReducer<Integer> integerMyReducer = new MyReducer<>();
        // 1 + 2 + ... + 10
        AtomicInteger x = new AtomicInteger(0);
        List<Integer> collect = Stream.generate(() -> {
            x.set(x.get() + 1);
            return x.get();
        }).limit(10).collect(Collectors.toList());
        Integer res = MapReducerTwo.mapReduce(collect.toArray(new Integer[0]),integerMyMapper,integerMyReducer);
        log.info("the result of mapReducer{}",res);
    }
    @Test
    public void mapReduceTest(){
        MyMapper<Integer> integerMyMapper = new MyMapper<>();
        MyReducer<Integer> integerMyReducer = new MyReducer<>();
        // 1 + 2 + ... + 10
        AtomicInteger x = new AtomicInteger(0);
        List<Integer> collect = Stream.generate(() -> {
            x.set(x.get() + 1);
            return x.get();
        }).limit(10).collect(Collectors.toList());
        Integer res = MapReducer.mapReduce(collect.toArray(new Integer[0]),integerMyMapper,integerMyReducer);
        log.info("the result of mapReducer{}",res);
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
        log.info("conguration , got it!!! {},{}", search1,search2);
    }

    @Test
    public void rightMoveTest() {
        int a = 1;
        a = a >>> 1; // 0
        // 0111 1111   2^32
        //
        int b = -1; //
        b = b >>> 1; // not -1 but 2147483647   sign 也要向右
        // 符号位 也要占一位  0 31个1
//        Integer.MAX_VALUE   2^31 -1 int 类型

        // 1111
        // 1111
        int c = -1;
        c = c >> 1; // not 1  but -1  sign 不要向右

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
