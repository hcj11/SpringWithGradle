package org.apache.http.client.nio;

import java.nio.channels.Pipe;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public class WakeupNow {

    public static void main(String[] args) throws Exception {
        test1();
        test2();
    }

    // Test if selectNow clears wakeup with the wakeup fd and
    // another fd in the selector.
    // This fails before the fix on Linux
    private static void test1() throws Exception {
        Selector sel = Selector.open();
        Pipe p = Pipe.open();
        p.source().configureBlocking(false);
        p.source().register(sel, SelectionKey.OP_READ);
        sel.wakeup();
        // ensure wakeup is consumed by selectNow
        Thread.sleep(2000);
        sel.selectNow();
        long startTime = System.currentTimeMillis();
        int n = sel.select(2000);
        long endTime = System.currentTimeMillis();
        p.source().close();
        p.sink().close();
        sel.close();
        if (endTime - startTime < 1000)
            throw new RuntimeException("test failed");
    }

    // Test if selectNow clears wakeup with only the wakeup fd
    // in the selector.
    // This fails before the fix on Solaris
    private static void test2() throws Exception {
        Selector sel = Selector.open();
        Pipe p = Pipe.open();
        p.source().configureBlocking(false);
        sel.wakeup();
        // ensure wakeup is consumed by selectNow
        Thread.sleep(2000);
        sel.selectNow();
        long startTime = System.currentTimeMillis();
        int n = sel.select(2000);
        long endTime = System.currentTimeMillis();
        sel.close();
        if (endTime - startTime < 1000)
            throw new RuntimeException("test failed");
    }

}