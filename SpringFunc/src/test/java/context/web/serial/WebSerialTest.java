package context.web.serial;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.SerialCustomBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
public class WebSerialTest {
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
