package Basic;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.util.HashMap;

public class JsonDemo {
    public static void main(String[] args) {
        HashMap hashMap = new HashMap();
        hashMap.put("1", Lists.<String>newArrayList("2","3"));
        String s = JSON.toJSONString(hashMap);
        System.out.println(s);
    }
}