package Collections;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Father<T> extends ArrayList{
    public void gets(){
    }
}

class Son extends Father {
    public <T extends Father> T get(T a,String aa) {
        return (T) get(1);
    }
}

public class Demo2 {

    private static void demo1() {
        String[] strings = new String[]{"hello", "world"};
        //
        List<String> strings1 = Arrays.asList(strings);
        try {
            strings1.add("no");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        strings[0] = "yes";
        String s = strings1.get(0);
        System.out.println(s);
    }

    public void demo2() {
        ArrayList<? super Father> fathers = Lists.<Father>newArrayList();
        fathers.add(new Father());
    }

    public static void main(String[] args) {
        new Demo2().demo2();
    }
}
