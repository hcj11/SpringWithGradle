package others;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Others {

    @Test
    public void load(){

        /**
         return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
         60L, TimeUnit.SECONDS,
         new SynchronousQueue<Runnable>());
         */
        ExecutorService executorService = Executors.newCachedThreadPool();

    }
}
