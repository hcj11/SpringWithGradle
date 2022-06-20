package stream;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
class User {
    private Integer age;
    private String name;

}

@Data
class AgeTreeMap<K, V> extends TreeMap {

    public AgeTreeMap() {
        super(comparatorGetByAge());
    }

    public static Comparator comparatorGet() {
        return (Comparator<User>) (o1, o2) -> {
            if (o1.getAge() > o2.getAge()) {
                return 1;  // ˳��
            } else if (o1.getAge() < o2.getAge()) {
                return -1;
            } else {
                return 0;
            }
        };
    }

    public static Comparator comparatorGetByAge() {
        return (Comparator<Integer>) (o1, o2) -> {
            if (o1 > o2) {
                return 1;  // ˳��
            } else if (o1 < o2) {
                return -1;
            } else {
                return 0;
            }
        };
    }
}

public class StreamDemo {

    public static void main(String[] args) {
        User hcj = User.builder().age(2).name("hcj2").build();
        User hcj2 = User.builder().age(3).name("hcj3").build();
        User hcj3 = User.builder().age(1).name("hcj1").build();
        ArrayList<User> users = Lists.<User>newArrayList(hcj, hcj2, hcj3);

//        AgeTreeMap<Integer, List<String>> collect = users.stream().
//                collect(Collectors.groupingBy(User::getAge, AgeTreeMap<Integer, List<String>>::new,
//                        mapping(User::getName, toList())));
//
//        Set<Map.Entry<Integer, List<String>>> set = (Set<Map.Entry<Integer, List<String>>>) collect.entrySet();
//        set.stream().forEach(col -> {
//            Integer key = col.getKey();
//            List<String> value = col.getValue();
//            System.out.println(col + "::" + value.toArray().toString());
//        });

    }
}
