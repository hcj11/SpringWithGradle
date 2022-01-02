package org.apache.http.client.nio;


import org.junit.jupiter.api.Test;

import java.nio.channels.ClosedSelectorException;
import java.nio.channels.Selector;

public class Wakeup {

    static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException x) {
            x.printStackTrace();
        }
    }

   static class Sleeper extends TestThread{
        volatile boolean started = false;
        volatile int entries = 0;
        volatile int wakeups = 0;
        volatile boolean wantInterrupt = false;
        volatile boolean gotInterrupt = false;
        volatile Exception exception = null;
        volatile boolean closed = false;
        Object gate = new Object();

        Selector sel;

        Sleeper(Selector sel) {
            super("Sleeper", System.err);
            this.sel = sel;
        }
        public void go() throws Exception {
            started = true;
            for (;;) {
                synchronized (gate) { }
                entries++;
                try {
                    sel.select();
                } catch (ClosedSelectorException x) {
                    closed = true;
                }
                boolean intr = Thread.currentThread().isInterrupted();
                wakeups++;
                System.err.println("Wakeup " + wakeups
                        + (closed ? " (closed)" : "")
                        + (intr ? " (intr)" : ""));
                if (wakeups > 1000)
                    throw new Exception("Too many wakeups");
                if (closed)
                    return;
                if (wantInterrupt) {
                    while (!Thread.interrupted())
                        Thread.yield();
                    gotInterrupt = true;
                    wantInterrupt = false;
                }
            }
        }

    }

    private static int checkedWakeups = 0;

    private static void check(Sleeper sleeper, boolean intr)
        throws Exception
    {
        checkedWakeups++;
        if (sleeper.wakeups > checkedWakeups) {
            sleeper.finish(100);
            throw new Exception("Sleeper has run ahead");
        }
        int n = 0;
        while (sleeper.wakeups < checkedWakeups) {
            sleep(50);
            if ((n += 50) > 1000) {
                sleeper.finish(100);
                throw new Exception("Sleeper appears to be dead ("
                                    + checkedWakeups + ")");
            }
        }
        if (sleeper.wakeups > checkedWakeups) {
            sleeper.finish(100);
            throw new Exception("Too many wakeups: Expected "
                                + checkedWakeups
                                + ", got " + sleeper.wakeups);
        }
        if (intr) {
            n = 0;
            // Interrupts can sometimes be delayed, so wait
            while (!sleeper.gotInterrupt) {
                sleep(50);
                if ((n += 50) > 1000) {
                    sleeper.finish(100);
                    throw new Exception("Interrupt never delivered");
                }
            }
            sleeper.gotInterrupt = false;
        }
        System.err.println("Check " + checkedWakeups
                           + (intr ? " (intr " + n + ")" : ""));
    }
    @Test
    public void test1() throws Exception {
        Selector sel = Selector.open();

        // Wakeup before select
        sel.wakeup();

        Sleeper sleeper = new Sleeper(sel);

        sleeper.start();
        while (!sleeper.started)
            sleep(50);

        check(sleeper, false);          // 1

        for (int i = 2; i < 5; i++) {
            // Wakeup during select
            sel.wakeup();
            check(sleeper, false);      // 2 .. 4
        }

        // Double wakeup
        synchronized (sleeper.gate) {
            sel.wakeup();
            check(sleeper, false);      // 5
            sel.wakeup();
            sel.wakeup();
        }
        check(sleeper, false);          // 6

        // Interrupt
        synchronized (sleeper.gate) {
            sleeper.wantInterrupt = true;
            sleeper.interrupt();
            check(sleeper, true);       // 7
        }

        // Interrupt before select
        while (sleeper.entries < 8)
            Thread.yield();
        synchronized (sleeper.gate) {
            sel.wakeup();
            check(sleeper, false);      // 8
            sleeper.wantInterrupt = true;
            sleeper.interrupt();
            sleep(50);
        }
        check(sleeper, true);           // 9

        // Close during select
        while (sleeper.entries < 10)
            Thread.yield();
        synchronized (sleeper.gate) {
            sel.close();
            check(sleeper, false);      // 10
        }

        if (sleeper.finish(200) == 0)
            throw new Exception("Test failed");
        if (!sleeper.closed)
            throw new Exception("Selector not closed");
    }


}