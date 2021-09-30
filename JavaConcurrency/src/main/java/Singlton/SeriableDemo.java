package Singlton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.HashMap;

@Slf4j
public class SeriableDemo implements Serializable {
    static final long serialVersionUID = 42L;
    static Singlton singlton = new Singlton();
    static SeriableDemo seriableDemo = new SeriableDemo();

    protected Object readResolve() throws ObjectStreamException{
        System.out.println("调用了readResolve 方法");
        return seriableDemo;
    }
    public static boolean odd(int i){
        return (i & 1) == 1;
    }
    public static boolean even(int i){
        return (i & 1) == 0;
    }
    public static void oddEvenTest(){
        System.out.println(odd(101));;
        System.out.println(odd(100));;
        System.out.println(odd(1));;
        System.out.println(even(2));;
    }
    public static void seriable()throws IOException, ClassNotFoundException{
        log.info("序列化前: {},{}",seriableDemo.hashCode(),seriableDemo.singlton.hashCode());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(seriableDemo);

        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        SeriableDemo o = (SeriableDemo)objectInputStream.readObject();
        log.info("序列化前: {},{}",o.hashCode(),o.singlton.hashCode());

    }
    public static void main(String[] args)  {
        HashMap<String, String> stringStringHashMap = Maps.<String, String>newHashMap();
        stringStringHashMap.put("1","2");
        stringStringHashMap.put("2","2");

        String s = JSONObject.toJSONString(stringStringHashMap);
        System.out.println(s);

    }
}
