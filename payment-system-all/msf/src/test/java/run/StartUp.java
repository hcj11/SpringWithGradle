package run;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ylink.msf.core.tools.BeanCopyUtil;
import com.ylink.msf.core.tools.MapUtil;
import com.ylink.upp.common.utils.UppEnumUtil;
import com.ylink.upp.common.utils.UppJsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.beans.BeanCopier;

import java.io.Serializable;
import java.util.Map;

interface SerialeInterface{
    @Data
    class Source implements Serializable {
        private String name;
        private String account;
    }
    @Data
    class Destinate implements Serializable{
        private String name;
        private String account;
    }
}
@Slf4j
public class StartUp {


    @Test
    public void test2(){

    }

    @Test
    public void test(){
        SerialeInterface.Source source = new SerialeInterface.Source();
        source.setAccount("中国人民银行账号");
        SerialeInterface.Destinate destinate = new SerialeInterface.Destinate();

        BeanCopyUtil.copy(source,destinate);
        log.info("{}",destinate.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES,false);

//        objectMapper.configure(JsonGenerator.Feature.,true);
        UppJsonUtil uppJsonUtil = new UppJsonUtil();
        uppJsonUtil.setObjectMapper(objectMapper);

        String s = uppJsonUtil.toJson(destinate);
        log.info("after tojson:{}",s);


        Map<String, Object> map = MapUtil.toMap(source);
        UppEnumUtil.translateValue(map);
        UppEnumUtil.translateValueByChl(map);

        String finalMap = uppJsonUtil.toJson(map);
        log.info("after tojson(Map):{}",finalMap);
//        objectMapper.configure()
//         UppJsonUtil.objectMapper = objectMapper;


    }

}
