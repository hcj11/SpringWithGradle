package CollectionDemo;

import com.google.common.collect.Maps;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * LinkeHashMapDemo  提供双向链表。记录hashmap下每个key的顺序。 默认按照插入顺序建立 链表连接
 */
class MapDemo implements Comparable , Comparator {
    private static int count = 0;
    private int id = count++;

    // 构造顺序先成员变量。最终是构造函数
    public MapDemo() {
    }

    public MapDemo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MapDemo{" +
                "id=" + id +
                '}';
    }

 // Compareable,  compareTo    两层排序。规则。
    @Override
    public int compare(Object o1, Object o2) {
        MapDemo next = (MapDemo) o2;
        return next.compareTo(o1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapDemo mapDemo = (MapDemo) o;
        return id == mapDemo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof MapDemo)) {
            return 0;
        }
        MapDemo o1 = (MapDemo) o;
        if (this.id > o1.id) {
            return -1; // 降序
        } else if (this.id < o1.id) {
            return 1;
        }
        return 0;
    }
}

public class LinkeHashMapDemo extends LinkedHashMap {
    public LinkeHashMapDemo(int i) {
        super(i,1f,true);
    }

    public static Map<MapDemo, String> mapTest(Map<MapDemo, String> map) {
        map.put(new MapDemo(), "haha");
        map.put(new MapDemo(), "haha");
        map.put(new MapDemo(), "haha");
        map.put(new MapDemo(), "haha2");
        Set<MapDemo> mapDemos = map.keySet();
        System.out.println(mapDemos);
        return map;
    }

    public static Map<MapDemo, String> linkHashMapDemoByAccessOrder() {
        return mapTest(new LinkedHashMap<>(16, 0.75f, true));
    }

    public static Map<MapDemo, String> linkHashMapDemo() {
        return mapTest(Maps.newLinkedHashMap());
    }

    public static Map<MapDemo, String> hashMapDemo() {
        return mapTest(Maps.newHashMap());
    }

    public static Map<MapDemo, String> treeMapDemo() {
        return mapTest(Maps.newTreeMap());
    }

    public static Map<MapDemo, String> linkedHashMapInsertOrderDemo() {
        return mapTest(Maps.newTreeMap());
    }
    public static void accessOrderDemo(){
        Map<MapDemo, String> mapDemoStringMap = linkHashMapDemoByAccessOrder();
        mapDemoStringMap.get(new MapDemo(1)); // lru, 根据访问顺序排列，当限制大小后，删除的考量
        Set<MapDemo> mapDemos = mapDemoStringMap.keySet();
        System.out.println(mapDemos);
    }
    /**
     * 根据访问顺序进行过期处理。
     * 根据插入元素的顺序进行排列, 并且根据访问顺序进行移动位置
     * lru 缓存，根据,
     */
    public static void accessOrderEvictDemo(){
        LinkeHashMapDemo linkeHashMapDemo = new LinkeHashMapDemo(16);
        LinkeHashMapDemo map = (LinkeHashMapDemo)mapTest(linkeHashMapDemo);
        // 被访问到后,放到末尾。
        map.get(new MapDemo(1));
        Set<MapDemo> mapDemos = map.keySet();
        System.out.println(mapDemos);

        map.put(new MapDemo(5),"hahah");
        Set<MapDemo> mapDemos2 = map.keySet();
        System.out.println(mapDemos2);
        System.out.println(mapDemos2.size());
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        // 判断是否删除。可以设置大小
        if (size() > 4) {
            return true;
        } else {
            return false;
        }
    }


    public static void main(String[] args) {
// 降序
        accessOrderEvictDemo();
    }
}
