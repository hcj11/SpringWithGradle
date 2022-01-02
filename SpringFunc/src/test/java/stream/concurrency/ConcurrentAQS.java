package stream.concurrency;

import cn.hutool.core.io.LineHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.stream.Stream;

@Slf4j
public class ConcurrentAQS {
   Object lock =  new Object();
    static class CustomSync extends AbstractQueuedSynchronizer {

        public CustomSync(int limit ){
            setState(limit);
        }

        @Override
        protected boolean tryAcquire(int arg) {

            int state = getState();;
            if(state==0){
                return false;
            }
            do {
                state = getState();
            }while (!compareAndSetState(state, state - arg));
            log.info("Acquire arg: {}",arg);
            return true;
        }

    }

    @Test
    public void test1() throws InterruptedException {
        LineHandler lineHandler = System.out::print;

        CustomSync customSync = new CustomSync(5);
        AtomicInteger atomicInteger = new AtomicInteger(1);
        Stream.generate(()->{return atomicInteger.incrementAndGet();}).parallel().forEach(ss->{
            customSync.tryAcquire(1);
            Thread.yield();;
            log.info("end:{}",customSync.toString()); // state=0 ,empty queue
        });

        synchronized (lock){lock.wait();}

    }
}
