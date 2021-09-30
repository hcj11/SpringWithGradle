package Mvc;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_EMPTY_JSON_ARRAYS;

@Slf4j
abstract class Parent {
    String required;
    public Parent(String required) {
        super();
        this.required = required;
    }
    public void say(){
        log.info("{}",required);
    }
}

@Slf4j
class Son extends Parent {
    public Son(String required) {
        super(required);
    }
    public static void main(String[] args) {
        Son yes = new Son("yes");
        yes.say();
    }
}

class CustomSerializer extends StdSerializer<Son> {
    protected CustomSerializer() {
        this(null);
    }
    protected CustomSerializer(Class t ) {
        super(t);
    }

    @Override
    public void serialize(Son value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        boolean b = gen.canWriteTypeId();
        gen.writeStartObject();
        gen.writeStringField("required","true");
        gen.writeEndObject();
    }
}


@JsonPropertyOrder({"phone", "hellow"})
@JsonIgnoreType
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true, value = "phone")
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonRootName(value = "", namespace = "")
public class Params {
    @JsonValue(value = true)
    @JsonSetter("description")
    Integer hellow;
    @JsonProperty("phone")
    @JsonIgnore(value = true)
    String phone;

    @Builder.Default
    List<Details> detailsList = Lists.<Details>newArrayList();
    @Builder.Default
    Details[] details = new Details[2];

    @Data
    class Details {
        private String number;
    }

    public static void demo1() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(WRITE_EMPTY_JSON_ARRAYS, true);
        Params build = Params.builder().phone("173****8371").detailsList(Lists.<Details>newArrayList())
                .build();
        String s = objectMapper.writeValueAsString(build);
        log.info("{}", s);

        ObjectWriter writer = objectMapper.writer();
        String s1 = writer.writeValueAsString(build);
        log.info("{}", s1);

        Params params = objectMapper.readValue(s, Params.class);
        log.info("{}", params);
    }

    public static void main(String[] args) throws JsonProcessingException, InvocationTargetException, IllegalAccessException {
        String params = "";
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module =
                new SimpleModule("CustomSerializer", new Version(1, 0, 0, null, null, null));
        module.addSerializer(Son.class,new CustomSerializer(Son.class));
        objectMapper.registerModule(module);



        ObjectReader reader = objectMapper.readerFor(TypeFactory.defaultInstance().constructCollectionLikeType(List.class, Params.class));
        List<Params> params1 = (List<Params>) reader.readValue(params);

        List<Params> params2 = objectMapper.readValue(params, new TypeReference<List<Params>>() {
        });

        BeanUtils.copyProperties("","","username","password");
        org.apache.commons.beanutils.BeanUtils.copyProperties("","");

        BeanUtilsBean.getInstance().copyProperties("","");
        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
        Object convert = convertUtilsBean.convert("", Params.class);



    }

}
