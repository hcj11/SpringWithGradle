package Quartz;

import Utils.Constants;
import org.quartz.*;

import static Utils.Constants.JOB_PARAM_KEY;


/**
 * 定时任务工具类
 *
 * @author Mark sunlightcs@gmail.com
 */
public class ScheduleUtils {
    private final static String JOB_NAME = "TASK_";

    /**
     * 获取触发器key
     */
    public static TriggerKey getTriggerKey(String jobId) {
        return TriggerKey.triggerKey(JOB_NAME + jobId);
    }

    /**
     * 获取jobKey
     */
    public static JobKey getJobKey(String jobId) {
        return JobKey.jobKey(JOB_NAME + jobId);
    }

    /**
     * 获取表达式触发器
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, String jobId) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        } catch (Exception e) {
            throw new RRException("获取定时任务CronTrigger出现异常", e);
        }
    }

    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, MessageDto scheduleJob) {
        try {
            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(getJobKey(scheduleJob.getJobId())).build();

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(scheduleJob.getJobId())).withSchedule(scheduleBuilder).build();

            //放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(JOB_PARAM_KEY, scheduleJob);

            scheduler.scheduleJob(jobDetail, trigger);

            //暂停任务
            if (scheduleJob.getStatus() == Constants.ScheduleStatus.PAUSE.getValue()) {
                pauseJob(scheduler, scheduleJob.getJobId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RRException("创建定时任务失败", e);
        }
    }


    /**
     * 立即执行任务
     */
    public static void run(Scheduler scheduler, MessageDto scheduleJob) {
        try {
            //参数
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(JOB_PARAM_KEY, scheduleJob);

            scheduler.triggerJob(getJobKey(scheduleJob.getJobId()), dataMap);
        } catch (Exception e) {
            throw new RRException("立即执行定时任务失败", e);
        }
    }

    /**
     * 暂停任务
     */
    public static void pauseJob(Scheduler scheduler, String jobId) {
        try {
            scheduler.pauseJob(getJobKey(jobId));
        } catch (Exception e) {
            throw new RRException("暂停定时任务失败", e);
        }
    }

    /**
     * 恢复任务
     */
    public static void resumeJob(Scheduler scheduler, String jobId) {
        try {
            scheduler.resumeJob(getJobKey(jobId));
        } catch (Exception e) {
            throw new RRException("暂停定时任务失败", e);
        }
    }

    /**
     * 删除定时任务
     */
    public static void deleteScheduleJob(Scheduler scheduler, String jobId) {
        try {
            scheduler.deleteJob(getJobKey(jobId));
        } catch (Exception e) {
            throw new RRException("删除定时任务失败", e);
        }
    }
}