package Mvc;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;


class CustomTypeIdResolver implements TypeIdResolver {

    @Override
    public void init(JavaType baseType) {

    }

    @Override
    public String idFromValue(Object value) {
        return null;
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return null;
    }

    @Override
    public String idFromBaseType() {
        return null;
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) throws IOException {
        return null;
    }

    @Override
    public String getDescForKnownTypeIds() {
        return null;
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}

class CustomJsonSerializer extends JsonSerializer {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        ;
        gen.writeObjectField("phone", "173****1111");
        gen.writeEndObject();
        ;
    }
}

class NullJsonSerializer extends JsonSerializer<Number> {

    @Override
    public void serialize(Number value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNumber(0);
        } else {
            gen.writeNumber((Integer) value);
        }
    }
}

/**
 * null -> number(0) -> 保证数据的有效。  ifnull(,0)  或者 COALESCE(null,0)
 */

@Slf4j
@Accessors(chain = true)
@JsonFormat(with = {JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,
        JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY
}
)
@Data
public class ObjectJson {


    @JsonCreator(mode = JsonCreator.Mode.DEFAULT)
    public ObjectJson() {
    }

    @NotNull
    @JsonTypeIdResolver(value = CustomTypeIdResolver.class)
    @JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM)
    private Long id;

    @JsonProperty("na")
    private String name;

    @JsonDeserialize
    @JsonSerialize(using = CustomJsonSerializer.class)
    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp timestamp = new Timestamp(new Date().getTime());

    @JsonSerialize(nullsUsing = NullJsonSerializer.class)
    private Integer numbers;

    /**
     * date / timestamp => 数据库中的日期型
     *
     * */ {
        DateUtil.isAM(new Date());
        DateUtil.beginOfDay(new Date());

    }

    public static void main(String[] args) {
        Character c = '\u0001';
        char ccc = '\u0002';
        byte ccc1 = (byte) ccc;
        log.info("{},{}", ccc,ccc1);

//        log.info("{},{}",DateUtil.beginOfDay(new Date()),DateUtil.offset(new Date()));
    }
}
