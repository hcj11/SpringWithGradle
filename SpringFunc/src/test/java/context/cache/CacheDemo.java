package context.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.*;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
@NoArgsConstructor
@AllArgsConstructor
@Data
class Test {
    private String name;
    private Integer no;
}
@ImportAutoConfiguration(classes = CacheAutoConfiguration.class)
@EnableCaching
@Configuration
class CacheConfiguration {

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager("cache");
        return concurrentMapCacheManager;
    }

}
@Caching(
        evict = @CacheEvict(
                key = "#root.args[0]", cacheNames = "cache",cacheManager = "cacheManager"),
        cacheable =
        @Cacheable(key = "#root.args[0]", cacheNames = "cache",cacheManager = "cacheManager" ,
                unless =
                "!#result.no.equals(-1)",condition = "#root.args[0] instanceof T(java.lang.String)")
            )
interface CacheInterface {
//    @Caching(put = @CachePut(cacheManager = "cacheManager"))
//    @Caching(cacheable = @Cacheable(cacheNames = "cache",cacheManager = "cacheManager"))
    public Test getTest(Integer no);
    public Test getName(String name);

}
@Slf4j
@Service
public class CacheDemo implements CacheInterface {
    @Autowired
    CacheManager cacheManager;
    public Test getTest(Integer no) {
        return new Test("",no);
    }

    @Override
    public Test getName(String name) {
        return new Test(name,-1);
    }

    @org.junit.jupiter.api.Test
    public void demo1() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CacheConfiguration.class,CacheDemo.class);
        CacheInterface bean = context.getBean(CacheInterface.class);
        Test test = bean.getTest(1);
        log.info("{}",test);
        Test test2 = bean.getTest(1);
//        Test test1 = bean.getName("sayHello");
//        log.info("{}",test1);
//        Test test3 = bean.getName("sayHello");
    }
}
