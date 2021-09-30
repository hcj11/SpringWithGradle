package CollectionDemo;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollectionTest {
    public static void sort() {
// list ������
        ArrayList<MapDemo> objects = Lists.newArrayList();
        objects.add(new MapDemo());
        objects.add(new MapDemo());
        objects.add(new MapDemo());
        System.out.println(objects);
        Collections.sort(objects); // Ĭ����id��������
        System.out.println(objects);
        // ��дcompareTo() ����ʵ�ֲ�ͬ��������� ��ǰһֱ��ʹ�ã�����û���ض��Ŀ��ǣ�����֤����
        Collections.sort(objects, new Comparator<MapDemo>() {
            @Override
            public int compare(MapDemo o1, MapDemo o2) {
                if (o1.getId() > o2.getId()) { // ����
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
        // ��ȷ����С��ֵ���Ա�����������������ռ�á�
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
