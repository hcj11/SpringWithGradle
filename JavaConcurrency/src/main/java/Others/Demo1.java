package Others;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.annotation.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * java 并发编程 锁分段技术。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface CustomKey {
    String name() default "hcj";
}

@AllArgsConstructor
@Data
class Pair<L, R> {
    private L key;
    private R value;
}

public class Demo1 {
    // [^d]\d+$ | ^d\d+$ |
    private static Pattern compile = Pattern.compile("([0-9]+)");

    public static void demo1() {

        List<Pair<String, Double>> pairArrayList = new ArrayList<>(3);
        pairArrayList.add(new Pair<>("version", 12.10));
        pairArrayList.add(new Pair<>("version", 12.19));
        pairArrayList.add(new Pair<>("version", 6.28));
        Map<String, Double> map = pairArrayList.stream().collect(
                // 生成的 map 集合中只有一个键值对：{version=6.28}
                Collectors.toMap(Pair::getKey, Pair::getValue, (v1, v2) -> v2));
        System.out.println(map);
    }

    public static void d() {
        boolean group = compile.matcher("123").matches();
        System.out.println(group);
        Matcher m = compile.matcher("123");
        if (m.find()) {
            System.out.println("Found value: " + m.group(0));
        } else {
            System.out.println("NO MATCH");
        }

    }

    public static void other() throws InterruptedException {
        int abs = Math.abs(-1);
        System.out.println(abs);
        double floor = Math.floor(5.4);
        System.out.println(floor);
        double ceil = Math.ceil(5.4);
        System.out.println(ceil);
        int i = Math.floorDiv(4, 3);
        System.out.println(i);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("shutdown.");
        }));
        Integer a = 1;
        Integer b = 2;
        demo1();
        Set<String> singleton = Collections.singleton("");
        ArrayList<Object> objects = Lists.newArrayList();
        objects.toArray(new Object[0]);
        objects.isEmpty();
        Collections.addAll(objects);

        ReentrantLock reentrantLock = new ReentrantLock();
        try {
            reentrantLock.lock();
        } finally {
            reentrantLock.unlock();
        }

        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
        countDownLatch.countDown();
    }

    public static void demo2() {

    }


    public static void main(String[] args) throws InterruptedException {
        // 包括该注解的
        Annotation[] annotations = Pattern.class.getAnnotations();

    }
}
