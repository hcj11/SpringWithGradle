package Collections;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;

import java.util.*;

class CustomHashMap extends TreeMap {

}

public class Demo3 {
    public static void main(String[] args) {
        Demo3 demo3 = new Demo3();
        demo3.list();

    }

    public void list() {
        ArrayList<A> as = Lists.<A>newArrayList(A.builder().name("hello").build(), A.builder().name("world").build());
        System.out.println(as);
        System.out.println(as);
        as.stream().parallel().forEach(System.out::println);
        as.stream().parallel().forEachOrdered(System.out::println);

    }

    public void keySet() {
        HashMap<String, String> map = Maps.<String, String>newHashMap();
        map.put("", "");
        Set<String> strings = map.keySet();
        // hashTable + 链表 / 树


    }

    public void iterator() {
        ArrayList<String> list = Lists.<String>newArrayList("1", "2");
        for (String s : list) {
            if (s.equals("1")) {
                list.remove(s);
            }
        }

        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            // 顺序不能乱， 应该是  next->remove
            System.out.println(iterator.next());
            iterator.remove();
        }
        System.out.println(list);
    }

    @Builder
    @Data
    static class A {
        private String name;

    }
}
