package Singlton;

import lombok.Data;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import static java.nio.charset.StandardCharsets.UTF_8;

@Data
public class Singlton implements Serializable {

    static final long serialVersionUID = 42L;
    private String name;

    public static class SingltonHolder {
        static Singlton singlton = new Singlton();
    }

    protected Object readResolve() throws ObjectStreamException {
        System.out.println("call Singlton 's readResolve method");
        return SingltonHolder.singlton;
    }

    public static void demo1() throws IOException, ClassNotFoundException {
        Singlton instance = SingltonHolder.singlton;
        instance.setName("hcj");
        System.out.println("序列化前: " + instance.hashCode());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(instance);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        Object o = objectInputStream.readObject();
        System.out.println("序列化后: " + o.hashCode());
    }


    public static void utilsConcurrentModificationException() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        HashMap<String, String> hashMap = new HashMap<String, String>() {{
            put("hcj1", "1");
            put("hcj2", "2");
            put("hcj3", "3");
        }};
//        Thread thread = new Thread(() -> {
//            hashMap.put("hcj4", "4");
//            countDownLatch.countDown();
//        });
//        thread.start();;

        Iterator<String> iterator = hashMap.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            iterator.remove();
            System.out.println(next);
        }
        System.out.println(hashMap);
    }

    public static void threadSafe() {
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<String, String>() {{
            put("hcj1", "hcj1");
        }};
        Iterator<Map.Entry<String, String>> iterator = concurrentHashMap.entrySet().iterator();
        Iterator<String> iterator1 = concurrentHashMap.keySet().iterator();
        while (iterator1.hasNext()) {
            String next = iterator1.next();
            iterator1.remove();
            System.out.println(next);
        }
        CopyOnWriteArrayList<String> objects = new CopyOnWriteArrayList<String>() {{
            add("hello .world, hcj!!!");
        }};
        objects.iterator().remove();
        ;
        Properties properties = new Properties();
        properties.setProperty("key", "线程安全");
        String key = (String) properties.getProperty("key");
        System.out.println(key);
    }

    public static void stringdemo1() {
        String str1 = "abc";
        String str2 = "a" + "bc";
        Assert.isTrue(str1 == str2);
        ; // true
    }

    public static void stringdemo2() {
        String str1 = "ab";
        String str2 = str1 + "c";
        String str3 = "abc";
        Assert.isTrue(str2 != str3);
    }

    public static void stringdemo3() {
        String str1 = new String("abc");
        // 创建两个对象
    }

    public static void stringdemo4() {
        String str1 = "abc";
        String str2 = "abc";
        Assert.isTrue(str2 == str1);
        ; // true
    }

    public static void stringdemo5() {
        String str1 = new String("abc");
        String str2 = "abc";
        Assert.isTrue(str2 != str1); // false
    }

    public static void stringdemo6() {
        String str2 = "abc";
        String str1 = new String("abc");
        Assert.isTrue(str2 != str1); // false
    }
    public static void stringdemo7() {
        String str2 = "ab"+"c";
        String str1 = new String("ab")+"c";
        Assert.isTrue(str2 != str1); // false
    }
    public static void main(String[] args) {
        byte[] bytes = Base64Utils.decodeFromString("eWFvbHVzaGFu");
        String s = new String(bytes, UTF_8);
        System.out.println(s);

    }

}
