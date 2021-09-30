package mapper;

import Mvc.domain.BeanObject;
import Mvc.ObjectJson;
import Mvc.domain.XmlObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.assertj.core.api.Assertions.assertThat;

interface MyInterface {
    String getString();
}

@Accessors(chain = true)
@Data
class TestBean implements MyInterface {
    private String string;
    private List<String> hellos;
    private Integer age;

}

@Slf4j
public class Jackson2ObjectMapperBuilderTest {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    Jaxb2RootElementHttpMessageConverter converterxml = new Jaxb2RootElementHttpMessageConverter();


    @Test
    public void demo3() {

        ConcurrentSkipListMap<Object, Object> map = new ConcurrentSkipListMap<>();
        new ConcurrentSkipListSet<>();

    }
    @Test
    public void readandwriteByXmlDetails() throws IOException {
        ArrayList<BeanObject.Properties> properties = Lists.<BeanObject.Properties>newArrayList(new BeanObject.Properties().setName("phone").setVal("173****1111"),
                new BeanObject.Properties().setName("age").setVal("13"));
        BeanObject xmlObject = new BeanObject().setBean(new BeanObject.Bean().setProperty(properties)).setList(Lists.newArrayList("1","2","3"));
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        converterxml.write(xmlObject,null,mockHttpOutputMessage);
        String bodyAsString = mockHttpOutputMessage.getBodyAsString(Charset.defaultCharset());
        log.info("{}",bodyAsString);
    }

    @Test
    public void readandwriteByXml() throws IOException {
        XmlObject xmlObject = new XmlObject().setSubname("sub").setRequiredEle("required");
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        converterxml.write(xmlObject,null,mockHttpOutputMessage);
        String bodyAsString = mockHttpOutputMessage.getBodyAsString(Charset.defaultCharset());
        log.info("{}",bodyAsString);
        MockHttpInputMessage mockHttpInputMessage = new MockHttpInputMessage(bodyAsString.getBytes());
        XmlObject read = (XmlObject)converterxml.read(XmlObject.class, mockHttpInputMessage);
        log.info("{}",read);

    }

    @Test
    public void readandwrite() throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        ObjectJson objectJson = new ObjectJson().setName("he").setPhone("hellowolrd").setNumbers(null);
        converter.write(objectJson,null,MediaType.APPLICATION_JSON,mockHttpOutputMessage);
        String bodyAsString = mockHttpOutputMessage.getBodyAsString(Charset.defaultCharset());
        log.info("{}",bodyAsString);

    }

    @Test
    public void readJson() throws IOException {

        ParameterizedTypeReference<TestBean> testbean = new ParameterizedTypeReference<TestBean>() {};

        String json="{\"string\": null,\"age\": 1,\"hellos\": [\"world\"]}";
        json = "{\n" +
                "    \"string\":null,\n" +
                "    \"age\":1,\n" +
                "    \"hellos\":[\"w\",\"o\",\"r\",\"l\",\"d\"]\n" +
                "}";
        MockHttpInputMessage mockHttpInputMessage = new MockHttpInputMessage(json.getBytes(Charset.defaultCharset()));
        HashMap read = (HashMap)converter.read(HashMap.class, null, mockHttpInputMessage);
        Integer integer = (Integer) read.get("age");
        ArrayList hellos = (ArrayList) read.get("hellos");
        log.info("{},{}",integer,hellos);
        /**
         * 多次读取导致失败，
         */
        MyInterface read1 = (MyInterface)converter.read(testbean.getType(),null, mockHttpInputMessage);
        log.info("{}", read1);
        assertThat(read1.getString()).isEqualTo(null);

    }


    @Test
    public void demo2() throws IOException {
        TestBean testBean = new TestBean().setString("hello world");
        MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
        converter.write(testBean, MyInterface.class, MediaType.APPLICATION_JSON, outputMessage);
        String bodyAsString = outputMessage.getBodyAsString(Charset.defaultCharset());
        HttpHeaders headers = outputMessage.getHeaders();
        log.info("{},{}", bodyAsString, headers);
    }

    @Test
    public void demo1() throws IOException {
        HashMap<Object, Object> map = Maps.newHashMap();
        map.put("hello", "world");
        map.put("merry", Lists.newArrayList(173));
        MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
        converter.write(map, MediaType.APPLICATION_JSON, outputMessage);
        String bodyAsString = outputMessage.getBodyAsString(Charset.defaultCharset());
        HttpHeaders headers = outputMessage.getHeaders();
        log.info("{},{}", bodyAsString, headers);
    }


}
