package sample.Simple;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.net.URLDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.zip.CRC32;
@Slf4j
public class DemoTest {
    @Test
    public void unEscapeTest(){
        char aaa ='\\';
        String str = "a\\\\\'r";
        String s = StringEscapeUtils.unescapeJava(str);
        log.info("unescapeJavaStr:{}",s);
        String str1 ="a\\\\\'\\r";
        String s1 = StringEscapeUtils.unescapeJava(str1);
        log.info("unescapeJavaStr1:{}",s1);

    }
    @Test
    public void try1(){
        new FutureTask<Callable>(null);

    }
    /**
     *
     */
    @Test
    public void urlEncoder(){
        String url="ZookeeperRegistry:  [DUBBO] Notify urls for subscribe url consumer://192.168.113.1/com.api.UserInterface?application=first-dubbo-consumer&category=providers,configurators,routers&dubbo=2.0.2&interface=com.api.UserInterface&logger=slf4j&methods=getName&pid=26692&release=2.7.5&revision=7f28-4f86-9085-2c0286c85af6-1.0-SNAPSHOT&side=consumer&sticky=false&timestamp=1641869628617, urls: [dubbo://192.168.113.1:20880/com.api.UserInterface?anyhost=true&application=first-dubbo-provider&default=true&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&interface=com.api.UserInterface&logger=slf4j&methods=getName&pid=26920&release=2.7.5&revision=3518d340-3975-4330-8963-800fa040a8ec-1.0-SNAPSHOT&side=provider&timestamp=1641868858044, empty://192.168.113.1/com.api.UserInterface?application=first-dubbo-consumer&category=configurators&dubbo=2.0.2&interface=com.api.UserInterface&logger=slf4j&methods=getName&pid=26692&release=2.7.5&revision=7f28-4f86-9085-2c0286c85af6-1.0-SNAPSHOT&side=consumer&sticky=false&timestamp=1641869628617, empty://192.168.113.1/com.api.UserInterface?application=first-dubbo-consumer&category=routers&dubbo=2.0.2&interface=com.api.UserInterface&logger=slf4j&methods=getName&pid=26692&release=2.7.5&revision=7f28-4f86-9085-2c0286c85af6-1.0-SNAPSHOT&side=consumer&sticky=false&timestamp=1641869628617], dubbo version: 2.7.5, current host: 192.168.113.1";
        String decode = URLDecoder.decode(url, StandardCharsets.UTF_8);
        System.out.println(decode);


    }
    @Test
    public void demo1(){
        String s = UUID.randomUUID().toString();
        CRC32 crc32 = new CRC32();
        crc32.update(s.getBytes(StandardCharsets.UTF_8));
        long value = crc32.getValue() ;
        // 0-9
        long mod= (long)value % 10;
        log.info("{},{}",value,mod);
    }
}
