package ParametersizedType;

import java.util.ArrayList;
import java.util.HashMap;

public class Demo {

    public static void main(String[] args) {
        ArrayList<String> strings = new ArrayList<>();
        ArrayList<Integer> integers = new ArrayList<>();
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("hello","world");
        System.out.println(stringStringHashMap.get("hello"));;

    }
}
