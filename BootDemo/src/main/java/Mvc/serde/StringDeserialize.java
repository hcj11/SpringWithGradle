package Mvc.serde;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;

public class StringDeserialize implements ObjectDeserializer {

    @Override
    public String deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String s = parser.parseObject(String.class);
        return s;
////        if(type.getTypeName().equalsIgnoreCase()){
////
////        }
//        if(String.class.isAssignableFrom(type.getClass())){
//            String parse = parser.parseObject(String.class);
//            if(StringUtils.isEmpty(parse)){
//                parse=null;
//            }
//        }else{
//            Type type1 = parser.parseObject(type.getClass());
//
//        }


    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;

    }
}