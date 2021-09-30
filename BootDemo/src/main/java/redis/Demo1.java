package redis;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * com.paic.redis 的互斥锁
 * 布隆过滤器
 * zset 排序功能
 */
@Getter
@Component
public class Demo1 {

    String key = "rank::";
    String otherkey = "other::";
    String destkey = "dest::";
    String mutex = "~lock";
    private RedisTemplate<String, String> template;
    private ZSetOperations<String, String> stringZSetOperations;

    public Demo1(RedisConnectionFactory connectionFactory, @Qualifier("stringRedisTemplate") RedisTemplate<String, String> template) {
        this.template = template;
        this.stringZSetOperations = template.opsForZSet();
        HashOperations<String, Object, Object> operations = template.opsForHash();


    }

    public void add() {// 1千万的数据如何建模。
        for (int i = 0; i < 100000; i++) {
            int i1 = new Random().nextInt(10) + 1;
            // 随机对某一个key下的value进行+1;
            stringZSetOperations.incrementScore(key, String.valueOf(i1), 1);
        }
    }

    public void pipleline() {
        template.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                BoundZSetOperations zSetOperations = operations.boundZSetOps(key);
                for (int i = 0; i < 10; i++) {
                    int i1 = new Random().nextInt(11) + 1;
                    zSetOperations.incrementScore(String.valueOf(i1), 1);
                }

                return operations.exec();
            }
        });
    }
    /**
     * hash(key) => hashKey
     */
    public void rank() {

        template.opsForHash().put("key","hashKey","value");



        stringZSetOperations.rank(key, 1);
        stringZSetOperations.range(key, 0, -1);
        stringZSetOperations.rangeByScore(key, 0, 10);

        stringZSetOperations.getOperations().expire(key, 100, TimeUnit.MILLISECONDS);
        stringZSetOperations.getOperations().expireAt(key, new Date());
        stringZSetOperations.getOperations().randomKey();
        // lru，手动编写lru key过期策略，
        // 超时剔除。
        // 主动更新。
    }
    /**
     * 缓存失效，导致流量打到 数据库上
     * 1. hystrix 降级
     * 2. 提高redis性能，为其开集群。
     */
    public void xuebeng(){

    }
    /**
     * 对热点key的问题  ，对于多个节点，同时去重建缓存，查询db  + 互斥锁，setNx
     * redission
     * 方法1. 设置分布式锁，保证只一个线程去重建，其他用老的缓存数据，
     * 方法2. 定期调整key的过期时间
     *
     */
    public void jichuan(){

        Random random = new Random();
        long l = random.nextLong()+1;
        Long expire = template.getExpire(key, TimeUnit.MILLISECONDS);
        long l1 = expire + l;
        template.expire(key,l1,TimeUnit.MILLISECONDS);

        // lua 随机生成一个值，可以保证， 不会删除错误。  即采用分布式锁。
        template.opsForValue().setIfAbsent(mutex,"",1,TimeUnit.MILLISECONDS);
    }

    public void inter(){
        Long aLong = stringZSetOperations.intersectAndStore(key, otherkey, destkey);
        System.out.println(aLong);
    }

    public void printScore() {
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringZSetOperations.rangeWithScores(key, -1, -1);
        typedTuples.forEach(tuples -> {
            String value = tuples.getValue();
            Double score = tuples.getScore();
            Set<String> strings = stringZSetOperations.rangeByScore(key, 0, score, 0, 5);
            System.out.println(strings);
        });

    }
    /**
     * 统计总量
     */
    public void hyperLogLog(){
        HyperLogLogOperations<String, String> loglog = template.opsForHyperLogLog();
        Long add = loglog.add("", "");
    }
    public void dd(){

    }
}
