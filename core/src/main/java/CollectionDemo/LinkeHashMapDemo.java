package CollectionDemo;

import com.google.common.collect.Maps;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * LinkeHashMapDemo  �ṩ˫��������¼hashmap��ÿ��key��˳�� Ĭ�ϰ��ղ���˳���� ��������
 */
class MapDemo implements Comparable , Comparator {
    private static int count = 0;
    private int id = count++;

    // ����˳���ȳ�Ա�����������ǹ��캯��
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

 // Compareable,  compareTo    �������򡣹���
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
            return -1; // ����
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
        mapDemoStringMap.get(new MapDemo(1)); // lru, ���ݷ���˳�����У������ƴ�С��ɾ���Ŀ���
        Set<MapDemo> mapDemos = mapDemoStringMap.keySet();
        System.out.println(mapDemos);
    }
    /**
     * ���ݷ���˳����й��ڴ���
     * ���ݲ���Ԫ�ص�˳���������, ���Ҹ��ݷ���˳������ƶ�λ��
     * lru ���棬����,
     */
    public static void accessOrderEvictDemo(){
        LinkeHashMapDemo linkeHashMapDemo = new LinkeHashMapDemo(16);
        LinkeHashMapDemo map = (LinkeHashMapDemo)mapTest(linkeHashMapDemo);
        // �����ʵ���,�ŵ�ĩβ��
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
        // �ж��Ƿ�ɾ�����������ô�С
        if (size() > 4) {
            return true;
        } else {
            return false;
        }
    }


    public static void main(String[] args) {
// ����
        accessOrderEvictDemo();
    }
}
