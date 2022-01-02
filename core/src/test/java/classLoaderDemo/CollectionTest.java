package classLoaderDemo;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CollectionTest {
    @Test
    public void test1(){
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


}
