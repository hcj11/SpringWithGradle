package jvm.gc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 3) -ea -Xss1m
 */
@Slf4j
public class TestStack {
    private int count;

    public TestStack() {

    }

    private void recusion(long a, long b, long c) {
        a = 0;
        b = 0;
        c = 0;
        count++;
        try {
            recusion(a, b, c);
        } catch (StackOverflowError e) {
            log.error("recusion 调用最大的栈深度" + count);
            return;
        }
    }

    @Test
    public void try1() {

        recusion(1, 2, 3);


    }
}
