package stream;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/*
* @author  houchunjian
* @date  2021/11/3 0003 14:08
* @param null
* @return
*/
@Slf4j
public class CompletableFutureTest {
    /**
     * check  CompletableFuture 's thread safe problem
     * multi  provider compute sum() result;
     *
     */

    @Test
    public Integer doAsync(final int val)  {
        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.completedFuture(1);
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        CompletableFuture<Integer> integerCompletableFuture1 = CompletableFuture.supplyAsync(() -> {
            return atomicInteger.get();
        })
                .thenCombine(integerCompletableFuture, (Integer i1, Integer i2) -> {
                    return i1 + i2 + val;
                });

        try {
            return integerCompletableFuture1.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void startUp(){

        final AtomicInteger atomicInteger = new AtomicInteger(0);
        Stream.generate(()->{return  atomicInteger.getAndIncrement();}).parallel().forEach(
                s->{
                    Integer res = doAsync(s);
                    Thread.yield();
                    // Ä¬ÈÏÊÇ
                    log.info("currentThread:{},res:{}",Thread.currentThread().getName(),res);
                    Assert.isTrue(res !=null && res == s + 1);

                }
        );
    }

}
