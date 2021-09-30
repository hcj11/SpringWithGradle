package other;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Data
class User{
    String username;
}
@Slf4j
public class Guava {
    public static void demo1() {
        Integer i = -1;
        System.out.println(Integer.toHexString(-1));
        /**
         * -1:
         * 11111111111111111111111111111111
         *
         */
        int ii = -1 << 29;
        System.out.println(ii | 0);
        AtomicInteger atomicInteger = new AtomicInteger(ii | 0);
        System.out.println(atomicInteger);
        TreeSet<String> strings = new TreeSet<>();
    }
    /**
     *连接器
     */
    private static Joiner joiner = Joiner.on(",").skipNulls();
    private static Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();

    public static void demo2() {
        String join = joiner.join("12", Lists.newArrayList(null, "321"));
        System.out.println(join);
        List<String> split = splitter.splitToList("1,null,2, ,12");
        split.stream().forEach(System.out::println);

    }
    public static void demo3(){
        String format=new String("222-2221");
        String initialValue=format;
        format = format.replaceAll("-", "");
        log.info("{},{}",format,initialValue);
    }
    public static void demo4(){
        char[] chars = new char[2];
        chars[0]=(char)1;
        chars[1]=(char)122222222222L;
        log.info("{}",chars);
    }
    public static void demo5(){
        LinkedListMultimap<Object, Object> multimap = LinkedListMultimap.create(1);
        /**
         *                         "com.google.common.collect.HashMultimap",
         *                         "com.google.common.collect.LinkedListMultimap",
         *                         "com.google.common.collect.LinkedHashMultimap",
         *                         "com.google.common.collect.ArrayListMultimap",
         *                         "com.google.common.collect.TreeMultimap"
         */
        Cache<Object, Object> build = CacheBuilder.
                newBuilder().maximumSize(10).concurrencyLevel(1).expireAfterAccess(1, TimeUnit.SECONDS)
                .build();
        CacheStats stats = build.stats();
        stats.requestCount();
        stats.evictionCount();


        Object o = new Object();
        int i = o.hashCode();
        System.out.println(i);

    }
    public static void demo6()throws IOException, URISyntaxException{

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        URL url1 = new URL("file://test.properties");

        try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url1}, contextClassLoader)) {
            InputStream resourceAsStream = urlClassLoader.getResourceAsStream("test_two_1.properties");
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            Object o = properties.get("tmp.files");
            System.out.println(o.toString());
        }

    }
    public static void main(String[] args) throws IOException, URISyntaxException {
        byte[] bytes = "password".getBytes(Charset.defaultCharset());
        HashMap<String, Object> map = Maps.<String, Object>newHashMap();
        map.put("context-type","application/json;charset=utf-8");
        DefaultClaims claims = (DefaultClaims) Jwts.claims();
        claims.putIfAbsent("username","好成绩");


        DefaultJwtBuilder defaultJwtBuilder = new DefaultJwtBuilder();

        String hcj = defaultJwtBuilder.setClaims(claims)
                .setHeaderParams(map).setAudience("hcj").setIssuedAt(new Date())
                .setExpiration(new Date())
                .signWith(SignatureAlgorithm.HS256,bytes).compact();

        // 解析 jwt
        Jws<Claims> claimsJws = (DefaultJws) Jwts.parser().setSigningKey(bytes).setAllowedClockSkewSeconds(3600).parseClaimsJws(hcj);
        Object username = claimsJws.getBody().get("username");
        log.info("{},{}",claimsJws,claimsJws.getSignature());







    }

}
