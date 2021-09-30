package other;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
@Slf4j
class AThread extends Thread{
    public RateLimiter rateLimiter;
    public AThread(RateLimiter rateLimiter){
        this.rateLimiter=rateLimiter;
    }
    @Override
    public void run() {
        super.run();
        boolean b = rateLimiter.tryAcquire(20, TimeUnit.SECONDS);
        if(b){
            double rate = rateLimiter.getRate();
            log.info("成功执行,{},{}",Thread.currentThread().getName(),rate);
        }else{
            log.info("被限流,{}",Thread.currentThread().getName());
        }
    }
}
/**
 * 单机限流，令牌桶
 */
public class RateLimiterDemo {

    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(20);

        IntStream.range(1,100).forEach(ints->{
            new AThread(rateLimiter).start();
        });
    }

}
