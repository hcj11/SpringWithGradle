package sample.gcJVM;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
class
 ARunable extends Thread{
    public ARunable(){
        super("Custom-Thread");
    }
    @Override
    public void run() {
        Calendar rightNow = Calendar.getInstance();
        rightNow.clone();

        log.info("=============hello , {}",Thread.currentThread().getName());
        super.run();
    }
}
@Slf4j
public class GCProblemAnalyise {
    @Test
    public void try0(){
        ArrayList<String> list = Lists.<String>newArrayList("1", "2");
        log.info("{}",list.toString());
    }

    @Test
    public void try1() throws InterruptedException {
        CompletableFuture completableFuture = CompletableFuture.runAsync(()->{});

        log.info("=======main method======hello , {}",Thread.currentThread().getName());
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new ARunable());
        Thread.sleep(10000000);
    }
    /**
     * -ea -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xmx512m -Xms512m -Xmn100m -XX:+UseConcMarkSweepGC
     */
    @Test
    public void ygcChange() {
     while (true) {
            com.thoughtworks.xstream.XStream xStream = new com.thoughtworks.xstream.XStream();
            xStream.toString();
            xStream = null;
        }
    }
    /**
     *   -ea -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintClassHistogramBeforeFullGC -XX:+PrintClassHistogramAfterFullGC  -Xmx512m -Xms512m -Xmn100m -XX:+UseConcMarkSweepGC
     */
    @Test
    public void ygcChangeGcImmediately() {
        int i=0;
        new ConcurrentHashMap<>();

        while (true) {
            i++;
            com.thoughtworks.xstream.XStream xStream = new com.thoughtworks.xstream.XStream();
            xStream.toString();
            xStream = null;
            if(i%1000==0){
                System.gc();
            }
        }
    }

}
