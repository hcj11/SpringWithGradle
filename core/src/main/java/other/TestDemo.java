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
//        ArrayList ��չ��1.5��
        int oldCapacity = 16;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        System.out.println(newCapacity);
        // 2��
//        LinkedList;
        int size = 16;
        int i = size >> 1;
        System.out.println(i);
//        HashMap
        // ABA:   ���ʱ�����
        AtomicStampedReference atomicStampedReference = new AtomicStampedReference("1", 11);
        atomicStampedReference.set(11, 111);
        atomicStampedReference.attemptStamp(1, 1);
        //���ڶ��������
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
        // �̼߳�Ĺ�ͨ��
        countDownLatch.await(); // +1
        countDownLatch.countDown(); // -1

    }

    public static void demo5() throws InterruptedException {
        // ��������  11 ��ȴ�
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
        // �������� ConcurrentModificationException �쳣.
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
        System.out.println(dd == aaaa); // ���õ�ַ��ͬ false
        System.out.println(dd == dddd); // ���õ�ַ��ͬ false
        System.out.println(dd == "a" + "a"); // ��"a"+"a"�Ľ��������������
    }

    /**
     * hashmap ��1.8���� ����+����������ķ�ʽ�Ľṹ�������ݵĴ�ȡ
     * Ĭ�ϲ��������С��������8�� ��ת�ɺ�����������¿ռ��С�� hash�� �����¼���key��λ�á�֮�����и������ݵ��¿ռ��£�
     * �������Ĭ�Ͽռ���64�� key�µ�value�����ظ������ݶ����hashcode���λ��  ����equals����String����ֱ�ӱȽ��ڴ�ռ�ķ�ʽ��
     * �ж��Ƿ�����滻��  ��key�����������е������β�� ���� ������ģ�ĳһλ�ã�
     * hashSet ʹ��hashmap��Ϊ���塣������ͬ��key��object��������ͬ�ģ� ֻ��Ƚ�key������
     * arrayList ���� ,   fast-fair ����ʧ�ܣ�  ���ڱ��������н�����ɾ�ĵ�����»��׳��쳣��  �ײ�����ݽṹ---���顣 *2
     * ʱ�临�Ӷ� log(n)
     * linklist ˫������֧��ͷ��β�Ĳ���͵����� �����ڲ���Ƶ���Ĳ����� ���ڶ���ͷ�Ͷ���β
     * ConcurrentHashMap 1.7���÷ֶ���+ hashEntry�������̰߳�ȫ���⣬��ִ��put����ʱ�������hash(key)λ���ĸ�segment���棬
     * ���Żᵽ �����в��Ҹ�key��λ�ã�������hashcode �� equals��ȫ��ȣ�������ͬ�Ķ���������������滻��������뵽β������������hashEntry
     * �򴴽�������С������ֵ����rehash�������ͻ��ʹ��������������������ͬһ���߳̿���ͬʱ���в�������ͬ���̶߳Բ�ͬ�ķֶ������в�������������
     * �����ڶ���̶߳�ͬһ���ֶ�����������������̣߳�ֻ����һ���߳̽��в�����
     * 1.8 ����cas���ֹ����� + synchronize����������  �����̵߳Ŀ��ơ�  ����+����+�����
     * casTabAt ���ֹ�����    �����ֳ�ͻʱͨ�� synchronize  ���п���
     * CopyOnWriteArrayList ʹ�����������п��ơ�
     * ReentrantLock �� synchronize ��֧���������� ��ֻ��ReentrantLock֧�ַ�������
     */

    public static void hashmap() {
        ReentrantLock reentrantLock = new ReentrantLock(false);
        HashSet<MapObj> objects2 = new HashSet<>(16, 0.75f);
        String sameHashCode = "����";
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
// 12 ����hash      32 *0.75 = 24

//        LinkedHashMap linkedHashMap =new LinkedHashMap(3,0.75f);


//        HashSet<Object> objects3 = new HashSet<>();
//        objects2.equals(objects3);
//        // �ж�set�����Ƿ����  ���ж�hashCode ��������ж�equals����
        LinkedList<Object> objects4 = new LinkedList<>();

        new HashMap<>(); // ����ʧ��
        ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<>();
        // ֻ�ǿ��ա�
        map.entrySet();
        map.remove("");


        // jvm ��ʱ���л��ա�
        // ��˭����
        // �����㷨
        // ���ղ���

    }
}

