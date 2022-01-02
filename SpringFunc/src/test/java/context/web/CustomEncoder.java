package context.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;

import java.lang.reflect.Type;

public class CustomEncoder implements Encoder {

    public CustomEncoder(){

    }
    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {

        template.body();

    }
}
