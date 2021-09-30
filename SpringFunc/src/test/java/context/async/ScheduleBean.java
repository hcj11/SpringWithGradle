package context.async;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;


@Data
class ReturnVal{
    private Character character = new Character('a');
}
@Slf4j
@EnableScheduling
@Configuration
public class ScheduleBean {

    @FunctionalInterface
    public interface CustomFunctionalInterface<T>{
        T getName();
    }


    public void doGetName(){
        /**
         * 策略模式。
         */
        CustomFunctionalInterface functionalInterface = ()->{return "hello world hcj!!!";};
        CustomFunctionalInterface functionalInterface2 = ()->{return "hello world !!!";};

        System.out.println(functionalInterface2.getName());

    }

    public void timerVsScheduleTask(){

    }
    public static void main(String[] args) {
        ScheduleBean scheduleBean = new ScheduleBean();
        scheduleBean.doGetName();;
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor =
                new ScheduledThreadPoolExecutor(10);
        scheduledThreadPoolExecutor.schedule(()->{},1, TimeUnit.DAYS);
        Timer timer = new Timer();

        String hello="hellow";

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.info("hello world,{}",hello);
            }
        },1000 *3);


    }
    static class CustomTrigger implements Trigger {
        @Override
        public Date nextExecutionTime(TriggerContext triggerContext) {

            Date date = triggerContext.lastCompletionTime();
            if(date==null){
            return new Date();
            }else{
                long l = date.getTime() + 1000;
                return new Date(l);
            }

        }

    }
    static class FixedBean {
        @Scheduled(fixedDelay = 5000)
        public Future<ReturnVal> fixedDelayMethod(){
            return new CompletableFuture<ReturnVal>();
        }
    }

    @Test
    public void ScheduleCombine(org.springframework.beans.factory.ObjectFactory<Object> objectObjectFactory) throws InterruptedException {


        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.initialize();;

        threadPoolTaskScheduler.schedule(()->{log.info("hello world!!!");},new CustomTrigger());
        Thread.sleep(1000000);

    }

    @Test
    public void schedule() throws InterruptedException {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.initialize();
        threadPoolTaskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                log.info("runing");
            }
        }, DateUtil.parse("2021-06-01 09:53:00"));
        Thread.sleep(1000000);
    }
    /**
     * use reflect get ScheduledTaskRegistrar
     *  find
     */
    @Test
    public void FixedParse() throws SchedulerException {
        /**
         * 扫描task; get cronable
         */
        JobDetail object1 = new JobDetailFactoryBean().getObject();

        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        JobKey hello = JobKey.jobKey("hello");

        schedulerFactoryBean.getObject().pauseJob(hello);


        ScheduledExecutorFactoryBean scheduledExecutorFactoryBean = new ScheduledExecutorFactoryBean();
        ScheduledExecutorService object = scheduledExecutorFactoryBean.getObject();
        /**
         *  autoconfigure =>  对conditional相关注解的处理
         *   multi - context ->  连接方式。
         *   使用代理，
         */
//

    }


}
