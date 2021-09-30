package other;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

@Builder
@Setter
@Getter
class MapObj {
    private String name;
    private String sameHashCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapObj mapObj = (MapObj) o;
        return name.equals(mapObj.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sameHashCode);
    }
}

public class TestDemo {
    static class X {
        public static Y y = new Y();
    }

    static class Y extends X {
        public Z z = new Z();
        private String name = "haha";
    }

    static class Z extends Y {
        public static String name = "hehe";
    }

    static void demo1() throws InterruptedException {
        //        System.out.println(X.y.name); // haha
//        System.out.println(X.y.z.name);//hehe
//        WeakHashMap weakHashMap = new WeakHashMap();
//        ArrayList<Object> objects = Lists.newArrayList();
//        objects.stream().forEach();
        // AQS
//        AbstractQueuedSynchronizer
//        ArrayList 扩展到1.5被
        int oldCapacity = 16;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        System.out.println(newCapacity);
        // 2倍
//        LinkedList;
        int size = 16;
        int i = size >> 1;
        System.out.println(i);
//        HashMap
        // ABA:   添加时间戳。
        AtomicStampedReference atomicStampedReference = new AtomicStampedReference("1", 11);
        atomicStampedReference.set(11, 111);
        atomicStampedReference.attemptStamp(1, 1);
        //对于对象的引用
        AtomicReference atomicReference = new AtomicReference();
        ReentrantLock reentrantLock = new ReentrantLock();
        HashMap map = new HashMap<String, MapObj>();
        Collections.synchronizedMap(map);
        new ConcurrentHashMap<>();
        new Hashtable<>();
//        Executors.newWorkStealingPool();
        Executors.newFixedThreadPool(1);
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue<>(1);
        CopyOnWriteArrayList<Object> objects = new CopyOnWriteArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        // 线程间的沟通。
        countDownLatch.await(); // +1
        countDownLatch.countDown(); // -1

    }

    public static void demo5() throws InterruptedException {
        // 插入排序  11 后等待
        PriorityQueue<Integer> integers = new PriorityQueue<Integer>(5,Integer::compareTo);
        integers.add(0);
        IntStream.rangeClosed(1,10).boxed().sequential().forEach(ss->integers.add(ss));
        integers.add(8);
        System.out.println(integers);

    }

    public static void demo2() throws InterruptedException {
        ArrayList<Object> objects = Lists.newArrayList();
        objects.add("1");
        objects.add("2");
        objects.stream().forEach(ss -> {
            // hasNext();
            objects.remove("2");
        });
        // 并发调整 ConcurrentModificationException 异常.
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(() -> {
            objects.remove("1");
            countDownLatch.countDown();
        }).start();
//
        Iterator<Object> iterator = objects.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            System.out.println(next);
            countDownLatch.await();
            Object next2 = iterator.next();
            System.out.println(next2);
        }
    }

    public static void demo3() {
        String s = "a" + "b";
        String ss = "ab";
        System.out.println(s == ss);
        System.out.println(s.equals(ss));
        String a = new String("dasd");
        String a2 = new String("dasd");
        System.out.println(a == a2); //false
        String b = "1";
        String bb = new String("1");
        System.out.println(b == bb);// false
        System.out.println(b.equals(bb)); // true
        String cc = "a";
        String dd = "aa";
        String aaaa = cc + new String("a");
        String dddd = new String("a") + cc;
        System.out.println(dd == aaaa); // 引用地址不同 false
        System.out.println(dd == dddd); // 引用地址不同 false
        System.out.println(dd == "a" + "a"); // 把"a"+"a"的结果丢到常量池中
    }

    /**
     * hashmap 在1.8采用 数组+链表、红黑树的方式的结构进行数据的存取
     * 默认采用数组大小，当大于8后 会转成红黑树（根据新空间大小和 hash与 来重新计算key的位置。之后会进行复制数据到新空间下）
     * 红黑树的默认空间是64， key下的value可以重复（根据对象的hashcode算成位置  根据equals或者String类型直接比较内存空间的方式）
     * 判断是否进行替换。  若key不相等则会排列到链表的尾部 或者 红黑树的（某一位置）
     * hashSet 使用hashmap作为主体。对于相同的key的object对象是相同的， 只需比较key，即可
     * arrayList 数组 ,   fast-fair 快速失败，  对于遍历过程中进行增删改的情况下会抛出异常。  底层的数据结构---数组。 *2
     * 时间复杂度 log(n)
     * linklist 双向链表，支持头和尾的插入和弹出。 适用于插入频繁的操作。 对于队列头和队列尾
     * ConcurrentHashMap 1.7采用分段锁+ hashEntry数组解决线程安全问题，当执行put操作时，会进行hash(key)位于哪个segment下面，
     * 接着会到 链表中查找该key的位置，若对象hashcode 和 equals完全相等，代表相同的对象的情况，会进行替换，否则插入到尾部，若不存在hashEntry
     * 则创建，若大小超过阈值，则rehash，减轻冲突，使用重入锁（悲观锁）当同一个线程可以同时进行操作，不同的线程对不同的分段锁进行操作而不会阻塞
     * 而对于多个线程对同一个分段锁会进行阻塞其他线程，只允许一个线程进行操作。
     * 1.8 采用cas（乐观锁） + synchronize（悲观锁）  进行线程的控制。  数组+链表+红黑树
     * casTabAt （乐观锁）    当出现冲突时通过 synchronize  进行控制
     * CopyOnWriteArrayList 使用重入锁进行控制、
     * ReentrantLock 和 synchronize 都支持重入锁， 而只有ReentrantLock支持非重入锁
     */

    public static void hashmap() {
        ReentrantLock reentrantLock = new ReentrantLock(false);
        HashSet<MapObj> objects2 = new HashSet<>(16, 0.75f);
        String sameHashCode = "哈哈";
        for (int i = 0; i < 10; i++) {
            objects2.add(MapObj.builder().sameHashCode(sameHashCode).name(String.valueOf(i)).build());
        }
        objects2.add(MapObj.builder().sameHashCode(sameHashCode).name(String.valueOf(11)).build());
        objects2.add(MapObj.builder().sameHashCode(sameHashCode).name(String.valueOf(12)).build());
        objects2.add(MapObj.builder().sameHashCode(sameHashCode).name(String.valueOf(13)).build());
        objects2.add(MapObj.builder().sameHashCode(sameHashCode).name(String.valueOf(14)).build());
    }

    public static void main(String[] args) throws InterruptedException {
        demo5();
        ArrayList<Object> objects = new ArrayList<>();
//        objects.add("1");

//        ArrayList<Object> objects1 = new ArrayList<>();
//        objects1.add("1");
//        boolean equals = objects.equals(objects1);
// 12 进行hash      32 *0.75 = 24

//        LinkedHashMap linkedHashMap =new LinkedHashMap(3,0.75f);


//        HashSet<Object> objects3 = new HashSet<>();
//        objects2.equals(objects3);
//        // 判断set对象是否相等  先判断hashCode 若相等在判断equals方法
        LinkedList<Object> objects4 = new LinkedList<>();

        new HashMap<>(); // 快速失败
        ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<>();
        // 只是快照、
        map.entrySet();
        map.remove("");


        // jvm 何时进行回收。
        // 对谁回收
        // 回收算法
        // 回收策略

    }
}

