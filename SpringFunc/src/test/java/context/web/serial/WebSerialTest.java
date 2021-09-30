package context.web.serial;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.SerialCustomBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
@Slf4j
public class WebSerialTest {

    @Test
    public void test1() throws JsonProcessingException {
        String json = "{\n" +
                "    \"data\":\"123\",\n" +
                "    \"subBean\" : {\n" +
                "        \"subData\":\"234\",\n" +
                "        \"date\":\"2021-11-12 00:00:00\"\n" +
                "    }\n" +
                "}"; // deserialize
        ObjectMapper objectMapper = new ObjectMapper();
        SerialCustomBean serialCustomBean = objectMapper.readValue(json, SerialCustomBean.class);
        log.info("{}",serialCustomBean);

    }
}
