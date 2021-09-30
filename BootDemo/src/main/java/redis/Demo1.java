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
 * com.paic.redis �Ļ�����
 * ��¡������
 * zset ������
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

    public void add() {// 1ǧ���������ν�ģ��
        for (int i = 0; i < 100000; i++) {
            int i1 = new Random().nextInt(10) + 1;
            // �����ĳһ��key�µ�value����+1;
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
        // lru���ֶ���дlru key���ڲ��ԣ�
        // ��ʱ�޳���
        // �������¡�
    }
    /**
     * ����ʧЧ������������ ���ݿ���
     * 1. hystrix ����
     * 2. ���redis���ܣ�Ϊ�俪��Ⱥ��
     */
    public void xuebeng(){

    }
    /**
     * ���ȵ�key������  �����ڶ���ڵ㣬ͬʱȥ�ؽ����棬��ѯdb  + ��������setNx
     * redission
     * ����1. ���÷ֲ�ʽ������ֻ֤һ���߳�ȥ�ؽ����������ϵĻ������ݣ�
     * ����2. ���ڵ���key�Ĺ���ʱ��
     *
     */
    public void jichuan(){

        Random random = new Random();
        long l = random.nextLong()+1;
        Long expire = template.getExpire(key, TimeUnit.MILLISECONDS);
        long l1 = expire + l;
        template.expire(key,l1,TimeUnit.MILLISECONDS);

        // lua �������һ��ֵ�����Ա�֤�� ����ɾ������  �����÷ֲ�ʽ����
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
     * ͳ������
     */
    public void hyperLogLog(){
        HyperLogLogOperations<String, String> loglog = template.opsForHyperLogLog();
        Long add = loglog.add("", "");
    }
    public void dd(){

    }
}
