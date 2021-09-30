package Simple;

import io.netty.util.concurrent.SingleThreadEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class CustomScheduledExecutor extends ScheduledThreadPoolExecutor {


    public CustomScheduledExecutor(){
        super(10);
    }
    protected <V> RunnableScheduledFuture<V> decorateTask(
                 Runnable r, RunnableScheduledFuture<V> task) {
        return new CustomTask<V>(r, task);
    }

    /**
     *
     */
    static class CustomTask<V> implements RunnableScheduledFuture<V> {
        public Runnable r;
        public RunnableScheduledFuture<V> task;
        public CustomTask(Runnable r, RunnableScheduledFuture<V> task) {
            this.r =r;
            this.task=task;
        }

        @Override
        public boolean isPeriodic() {
            return false;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return 0;
        }

        @Override
        public int compareTo(Delayed o) {
            return 0;
        }

        @Override
        public void run() {
            r.run();
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return false;
        }

        @Override
        public V get() throws InterruptedException, ExecutionException {
            log.info("---{}-0---",Thread.currentThread().getName());
            return task.get();
        }

        @Override
        public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return task.get(timeout, unit);
        }
    }
    public void defaultScheduleExecutor() throws ExecutionException, InterruptedException {
        /**
         * д╛хо
         */
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor  =new ScheduledThreadPoolExecutor(10);
        ScheduledFuture<String> schedule = scheduledThreadPoolExecutor.schedule(() -> {
            return  Thread.currentThread().getName()+"----hello world!!!";
        }, 3, TimeUnit.SECONDS);
        log.info("{}",schedule.get());;
    }
    public void decorateTask() throws ExecutionException, InterruptedException {
        CustomScheduledExecutor customScheduledExecutor = new CustomScheduledExecutor();
        RunnableScheduledFuture<Object> objectRunnableScheduledFuture = customScheduledExecutor.decorateTask(() -> {
        }, null);
        objectRunnableScheduledFuture.run();
        log.info("{}",objectRunnableScheduledFuture.get());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CustomScheduledExecutor customScheduledExecutor = new CustomScheduledExecutor();
        customScheduledExecutor.defaultScheduleExecutor();
//        customScheduledExecutor.decorateTask();


    }
 
}
