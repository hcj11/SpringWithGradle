package CollectionDemo;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollectionTest {
    public static void sort() {
// list 的排序。
        ArrayList<MapDemo> objects = Lists.newArrayList();
        objects.add(new MapDemo());
        objects.add(new MapDemo());
        objects.add(new MapDemo());
        System.out.println(objects);
        Collections.sort(objects); // 默认以id降序排序，
        System.out.println(objects);
        // 重写compareTo() 方法实现不同的排序规则， 以前一直在使用，但是没有特定的考虑，现在证明了
        Collections.sort(objects, new Comparator<MapDemo>() {
            @Override
            public int compare(MapDemo o1, MapDemo o2) {
                if (o1.getId() > o2.getId()) { // 降序
                    return -1;
                } else if (o1.getId() < o2.getId()) {
                    return 1;
                }
                return 0;
            }
        });
        System.out.println(objects);;
    }

    public static void main(String[] args) {
//        HashSet
//        HashMap
//        Hashtable
//        ConcurrentHashMap


    }

    public static void other() {
        // ensureCapacity
        ArrayList<String> strings = new ArrayList<String>();
        // 先确认最小阈值，以便无需进行数组的重新占用。
        strings.ensureCapacity(1000000);
        strings.add("11");
        strings.addAll(new ArrayList<String>() {
            {
                add("2");
            }
        });
//          although some implementations, such as
//         * {@link LinkedList}, do not prohibit insertion of {@code null}.
        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.offer(null);
        System.out.println(linkedList.get(0));

        PriorityQueue<Integer> integers = new PriorityQueue<Integer>();
        integers.add(5);
        integers.add(1);
        integers.add(2);
        Integer poll = integers.poll();
        System.out.println(poll);

        System.out.println("old" + integers);
        System.out.println("new" + integers);
        Integer[] integers1 = {1, 2, 4};
    }
}
