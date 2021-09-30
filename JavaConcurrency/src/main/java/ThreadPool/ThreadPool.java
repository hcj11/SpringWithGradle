package ThreadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

class CustomThreadFactory implements ThreadFactory {
    private static int i;

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName("" + i++);
        return thread;
    }
}
@Slf4j
class CustomCommonTimeTask extends TimerTask {
    private static int i;

    @Override
    public void run() {
        String format = String.format("Common: %s,%s", Thread.currentThread().getName(), i++);
        log.info(format);
    }
}
@Slf4j
class CustomTimeTask extends TimerTask {
    private static int i;

    @Override
    public void run() {
        String format = String.format("%s,%s", Thread.currentThread().getName(), i++);
        log.info(format);
        if (i == 30) {
            throw new RuntimeException("intend");
        }
    }
}

public class ThreadPool {
    public void timer() {
        Timer timer = new Timer();
        timer.schedule(new CustomTimeTask(), 10, 5 );
        timer.schedule(new CustomCommonTimeTask(), 10, 5 );

    }

    public void schedule() {

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(
                10, new CustomThreadFactory());
        scheduledExecutorService.scheduleAtFixedRate(new CustomTimeTask(),
                1L, 1L, TimeUnit.SECONDS);
        scheduledExecutorService.scheduleAtFixedRate(new CustomCommonTimeTask(),
                1L, 1L, TimeUnit.SECONDS);
    }


    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool();
        threadPool.timer();
    }
}
