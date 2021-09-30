package context.async.manager;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 *  servlet asyncManager
 */
@Slf4j
public class AsyncManager {
    @Test
    public void demo1(){
//        new WebAsyncTask<>();
//        RequestMappingHandlerAdapter
    }
    @Test
    public void load() throws IOException {
        String handlerMappingsLocation = "META-INF/spring.handlers";
        ClassLoader classLoader = this.getClass().getClassLoader();
        Properties mappings =
                PropertiesLoaderUtils.loadAllProperties(handlerMappingsLocation,classLoader);
        log.info("{}",mappings);


    }
}
