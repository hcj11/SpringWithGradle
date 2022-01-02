package stream.forEach;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Deque;
import java.util.Queue;
import java.util.Spliterator;
import java.util.concurrent.LinkedBlockingDeque;

/**
* @author  houchunjian
* @date  2021/11/3 0003 15:48
* @param null
* @return
*/
@Slf4j
public class SpliteratorTest {

    @Test
    public void test1(){
        // estimate >>>= 1
        long max =  Long.MAX_VALUE;
        while (max!=0){
            max >>>=1;
        }
        log.info("{}",max);

        LinkedBlockingDeque<String> strings = new LinkedBlockingDeque<>();
//        strings.spliterator().forEachRemaining();

        Spliterator<String> stringSpliterator = strings.spliterator().trySplit();

//        Queue<String> queue = Collections.asLifoQueue(strings);
//        queue.add("");
//

    }
}
