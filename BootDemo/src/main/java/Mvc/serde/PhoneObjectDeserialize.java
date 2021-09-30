package Mvc.serde;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;

public class PhoneObjectDeserialize implements ObjectDeserializer {

    @Override
    public String deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String parse = parser.parseObject(String.class);
        if(StringUtils.isBlank(parse)){
            parse="ÐÂÔö";
        }
        return parse;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;

    }
}
