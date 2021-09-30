package other;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import org.springframework.util.Base64Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class Operate {
    public static void demo1(int num) {
        // 对于可以整除、
        int res = num;
        if ((res = res % 3) == 0) {
            while ((res = res / 3) != 0) {
                System.out.println(res);
            }
            return;
        }

    }

    public static void demo2(int num) {
        int res = num;
        while ((res = res / 3) != 0) {

        }

    }

    @Data
    static class A {
        private String name;

    }

    public static void demo4() {
        JSONObject jsonObject = JSONObject.parseObject(null);

    }

    public static void demo3() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        A a = new A();
        a.setName("hello world.");
        String s = objectMapper.writeValueAsString(a);
        A a1 = objectMapper.readValue(s, A.class);
        JsonNode jsonNode = objectMapper.readTree(s);
        String name = jsonNode.get("name").asText();
        System.out.println(name);
    }

    public static void demo5() throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String appKey = "hcjkey";
        String secret = "hcjsecret";
        String timestamp = System.currentTimeMillis() + 1500 + "";
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        byte[] bytes = secret.getBytes("UTF-8");
        hmacSHA256.init(new SecretKeySpec(bytes, 0, bytes.length, "HmacSHA256"));
        String textTosign = timestamp + UUID.randomUUID().toString().replaceAll("|", "");
        String s = Base64Utils.encodeToString(hmacSHA256.doFinal(textTosign.getBytes("UTF-8")));
        System.out.println(s);
        String s1 = Base64Utils.encodeToString(hmacSHA256.doFinal(textTosign.getBytes("UTF-8")));
        System.out.println(s1);
    }

    public static void demo6() {
        ArrayList<String> list = Lists.<String>newArrayList("1", "2", "3");
        LinkedHashSet<String> objects = Sets.<String>newLinkedHashSet(list);
        String[] strings = objects.toArray(new String[0]);
        Arrays.stream(strings).forEach(System.out::println);
    }

    public static void demo7() {
        SecureRandom secureRandom = new SecureRandom();
        int i = secureRandom.nextInt(2);
        Random random = new Random();
        System.out.println(random.nextInt(2) + ":" + i);
        ;
    }

    public static void demo8() {
        SecureRandom secureRandom = new SecureRandom();
        double i = secureRandom.nextDouble();
        Random random = new Random();
        System.out.println(random.nextInt(2) + ":" + i);

    }

    public static void demo9() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("md5");
        md5.update("17317298371".getBytes("UTF-8"));
        byte[] digest = md5.digest();

        String hex = (new HexBinaryAdapter()).marshal(md5.digest("17317298371".getBytes()));
        System.out.println(hex);
    }

    public static void demo10(){
        //  2 ^ 9 |   2 ^ 10 =1024
        String format = String.format("%03d,%03d", 11,111);
        System.out.println(format);

        String format1 = String.format("%s,%03d", "11",111);
        System.out.println(format1);
    }
    public static void demo11(){
        char[] chars = {'1','0'};
        Character character = new Character(chars[1]);
        char c = character.charValue();
        System.out.println(c);
        final String s = character.toString();
    }
    public static void main(String[] args) throws Exception {
        List<Long> longs = Lists.newArrayList(1L, 2L);
        String s = JSON.toJSONString(longs);
        JSONArray objects = JSON.parseArray(s);
        int size = objects.size();
        for (int i=0;i<size;i++){
            Long o = objects.getLong(i);
            System.out.println(o.toString());
        }

    }

}
