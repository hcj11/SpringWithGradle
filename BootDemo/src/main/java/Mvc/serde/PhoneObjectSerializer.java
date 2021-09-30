package Mvc.serde;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class PhoneObjectSerializer implements ObjectSerializer {

        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {

            if(String.valueOf(object).equals("17317298371")){
                serializer.writeNull();
            }else{
                serializer.write(object);
            }

        }
    }