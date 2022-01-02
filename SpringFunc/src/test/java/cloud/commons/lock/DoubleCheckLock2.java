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
public class DoubleCheckLock2 {

    /**
     * the class is loaded  when method invoke.
     * what time nested class is load ?   answer: when it's be referenced;
     * compiler 优化， lazy initlization.
     */
    static class LockHolder{
        static {
            System.out.println("staic LockHolder is loaded");
        }
        public static DoubleCheckLock2 doubleCheckLock2 = new DoubleCheckLock2();

    }
    public static  DoubleCheckLock2 getLockHolder(){
        return LockHolder.doubleCheckLock2;
    }


    @Test
    public void test1() {
        AtomicInteger s = new AtomicInteger();

        // singleton  odd : (s & 1) ==1
        Stream.generate(() -> {
            return s.getAndIncrement();
        }).parallel().filter(ss -> {
            return (ss & 1) == 1;
        }).limit(1).forEach(i -> {
            DoubleCheckLock2 doubleCheckLock2 = new DoubleCheckLock2();

            DoubleCheckLock2 lockHolder = DoubleCheckLock2.getLockHolder();
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
