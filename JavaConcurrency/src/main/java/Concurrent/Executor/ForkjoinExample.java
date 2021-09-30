package Concurrent.Executor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * forkJoin 并行计算1-100的值
 */
@EqualsAndHashCode(callSuper=false)
@Slf4j
@Data
public class ForkjoinExample extends RecursiveTask<Integer> {
    private final int first;
    private final int last;
    private static final int theshold=4;

    @Override
    protected Integer compute() {
        if (last - first <= theshold) {
            log.info("{},{}",first,last);
            int res = 0;
            for (int i = first; i <= last; i++) {
                res+=i;
            }
            return res;
        } else {
            int middle = first + (last  - first) / 2;
            ForkjoinExample forkjoinExampleLeft = new ForkjoinExample(first, middle);
            ForkjoinExample forkjoinExampleRight = new ForkjoinExample(middle + 1, last);
            ForkJoinTask<Integer> fork = forkjoinExampleLeft.fork();
            ForkJoinTask<Integer> fork1 = forkjoinExampleRight.fork();
            return fork.join() + fork1.join();

        }
    }
    /**
     * 并行计算，每个线程对应一个队列， 不会产生线程安全问题。
     */
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        ForkjoinExample forkjoinExample = new ForkjoinExample(1, 100);
        forkJoinPool.execute(forkjoinExample);
        Integer join = forkjoinExample.join();
        System.out.println(join);

        int result = forkJoinPool.invoke(forkjoinExample);
        System.out.println(result);

    }
}
