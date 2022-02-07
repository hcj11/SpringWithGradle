package context.web.serial;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import context.domain.SerialCustomBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class WebSerialTest {
    @Test
    public void matcherAnyOneTest(){
        // the length decided by Unicode code units
        String regex= "dsa*?";
        String input = "[]dsa\\、。、？、";
        Assert.isTrue(input.length()==11);

        Pattern compile = Pattern.compile("、|。|？");
        Matcher matcher = compile.matcher(input);
        String s = matcher.replaceAll("");
        Assert.isTrue(s.equals("[]dsa\\"));

    }

    @Test
    public  void matcherEveryOneTest(){
        String toBeTest = "[]dsa\\、。、？、";
        String s = toBeTest.replaceAll("、。、？", "");
        log.info("the str after replace,{}",s);
        Assert.isTrue(s.equals("[]dsa\\"));
//        String regex="、。、？";
//        boolean match = Matcher.match(regex, toBeTest, false);


    }
    @Test
    public void ConverterTest(){
//        LongTaskTimingHandlerInterceptor.class;

        Class<AbstractGenericHttpMessageConverter> abstractGenericHttpMessageConverterClass = AbstractGenericHttpMessageConverter.class;
//        Converter.class;
//        ConverterFactory.class;
        Class<Converter> converterClass = Converter.class;
        RedisCustomConversions redisCustomConversions = new RedisCustomConversions();
//        redisCustomConversions.

    }
    @Test
    public void writeTest() throws IOException {
        SerialCustomBean serialCustomBean = new SerialCustomBean();
        serialCustomBean.setData("super test");
        serialCustomBean.getSubBean().setSubData("sub test");
        serialCustomBean.getSubBean().setDate(LocalDateTime.now().toString());

    // DataOutput
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(byteArrayOutputStream,serialCustomBean);

        String s = byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());
        log.info("{}",s);

    }

    @Test
    public void test1() throws JsonProcessingException {

        String json = "{\n" +
                "    \"data\":\"123\",\n" +
                "    \"subBean\" : {\n" +
                "        \"subData\":\"234\",\n" +
                "        \"date\":\"2021-11-12 00:00:00\"\n" +
                "    }\n" +
                "}"; // deserialize
        json = " {\"data\":\"super test\",\"subBean\":{\"subData\":\"sub test\",\"date\":\"2021-10-10T12:58:27.549\"}}";
        json = "{\"data\":\"super test\",\"subBean\":{\"serialCustomBean\":null,\"subData\":\"sub test\",\"date\":\"2021-10-10T13:01:40.163\"}}";
        ObjectMapper objectMapper = new ObjectMapper();
        SerialCustomBean serialCustomBean = objectMapper.readValue(json, SerialCustomBean.class);
        log.info("{}",serialCustomBean);

    }
}
