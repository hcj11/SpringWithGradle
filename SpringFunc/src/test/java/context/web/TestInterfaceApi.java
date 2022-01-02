package context.web;

import com.CustomDtoWithScope;
import feign.*;

public interface TestInterfaceApi {

    @Headers(value = {"content-type: application/json","Accept: text/plain"})
    @RequestLine(value = "POST /")
    Response post(CustomDtoWithScope customDtoWithScope);



}
