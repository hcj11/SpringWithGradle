package redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Config {
    @Bean
    public RedisTemplate<String, Integer> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }


    public static void demo1(Demo1 bean) {
        bean.add();
    }

    public static void demo2(Demo1 bean) {
        // 定时拉取key
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(()->{
            bean.printScore();
        },1,3, TimeUnit.SECONDS);
    }
    public static void demo3(Demo1 bean){
        bean.inter();; // 根据key，进行求交集
    }

    public static void main(String[] args) {
        // 由Config主源启动一个 ApplicationContext
        ConfigurableApplicationContext run = SpringApplication.run(Config.class, args);
        Demo1 bean = run.getBean(Demo1.class);
        demo3(bean);
        bean.add();

    }

}
