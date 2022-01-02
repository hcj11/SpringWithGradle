package cloud.commons.lock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Slf4j
public class DoubleCheckLock {
    private static volatile Singleton singleton;

    public Singleton getSingleton() {
        if (singleton == null) {
            synchronized (this) {
                Thread.yield();
                if (singleton == null) {
                    singleton = new Singleton("1");
                    return singleton;
                }

            }
        }
        return singleton;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public class Singleton {
        private String name;

    }

    @Test
    public void test1() {
        AtomicInteger s = new AtomicInteger();
//        Stream.generate(() -> {
//            return s.getAndIncrement();
//        }).forEach((sss)->{});
        HashSet<Singleton> singletons = new HashSet<>();


        // singleton  odd : (s & 1) ==1
        Stream.generate(() -> {
            return s.getAndIncrement();
        }).parallel().filter(ss -> {
            return (ss & 1) == 1;
        }).forEach(i -> {
            if (DoubleCheckLock.singleton == null) {
                DoubleCheckLock doubleCheckLock = new DoubleCheckLock();
                Singleton singleton = doubleCheckLock.getSingleton();
                log.info("{}", singleton != null);
            } else {
                singletons.add(DoubleCheckLock.singleton); //
                log.info("line:{},size:{},singleton:{}", i, singletons.size(), DoubleCheckLock.singleton);
            }
        });

//        Stream.generate(() -> {
//            return s.getAndIncrement();
//        }).limit(10).parallel().forEach(i->{
//            if (DoubleCheckLock.singleton == null) {
//                DoubleCheckLock doubleCheckLock = new DoubleCheckLock();
//                Singleton singleton = doubleCheckLock.getSingleton();
//                log.info("{}", singleton != null);
//            }else{
//                singletons.add(DoubleCheckLock.singleton ); //
//                log.info("line:{},size:{},singleton:{}", i,singletons.size(),DoubleCheckLock.singleton);
//            }
//        });


    }
}
