package Quartz;

import Utils.Utils.Constants;
import org.quartz.*;

import static Utils.Utils.Constants.JOB_PARAM_KEY;


/**
 * ��ʱ���񹤾���
 *
 * @author Mark sunlightcs@gmail.com
 */
public class ScheduleUtils {
    private final static String JOB_NAME = "TASK_";

    /**
     * ��ȡ������key
     */
    public static TriggerKey getTriggerKey(String jobId) {
        return TriggerKey.triggerKey(JOB_NAME + jobId);
    }

    /**
     * ��ȡjobKey
     */
    public static JobKey getJobKey(String jobId) {
        return JobKey.jobKey(JOB_NAME + jobId);
    }

    /**
     * ��ȡ���ʽ������
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, String jobId) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        } catch (Exception e) {
            throw new RRException("��ȡ��ʱ����CronTrigger�����쳣", e);
        }
    }

    /**
     * ������ʱ����
     */
    public static void createScheduleJob(Scheduler scheduler, MessageDto scheduleJob) {
        try {
            //����job��Ϣ
            JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(getJobKey(scheduleJob.getJobId())).build();

            //���ʽ���ȹ�����
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();

            //���µ�cronExpression���ʽ����һ���µ�trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(scheduleJob.getJobId())).withSchedule(scheduleBuilder).build();

            //�������������ʱ�ķ������Ի�ȡ
            jobDetail.getJobDataMap().put(JOB_PARAM_KEY, scheduleJob);

            scheduler.scheduleJob(jobDetail, trigger);

            //��ͣ����
            if (scheduleJob.getStatus() == Constants.ScheduleStatus.PAUSE.getValue()) {
                pauseJob(scheduler, scheduleJob.getJobId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RRException("������ʱ����ʧ��", e);
        }
    }


    /**
     * ����ִ������
     */
    public static void run(Scheduler scheduler, MessageDto scheduleJob) {
        try {
            //����
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(JOB_PARAM_KEY, scheduleJob);

            scheduler.triggerJob(getJobKey(scheduleJob.getJobId()), dataMap);
        } catch (Exception e) {
            throw new RRException("����ִ�ж�ʱ����ʧ��", e);
        }
    }

    /**
     * ��ͣ����
     */
    public static void pauseJob(Scheduler scheduler, String jobId) {
        try {
            scheduler.pauseJob(getJobKey(jobId));
        } catch (Exception e) {
            throw new RRException("��ͣ��ʱ����ʧ��", e);
        }
    }

    /**
     * �ָ�����
     */
    public static void resumeJob(Scheduler scheduler, String jobId) {
        try {
            scheduler.resumeJob(getJobKey(jobId));
        } catch (Exception e) {
            throw new RRException("��ͣ��ʱ����ʧ��", e);
        }
    }

    /**
     * ɾ����ʱ����
     */
    public static void deleteScheduleJob(Scheduler scheduler, String jobId) {
        try {
            scheduler.deleteJob(getJobKey(jobId));
        } catch (Exception e) {
            throw new RRException("ɾ����ʱ����ʧ��", e);
        }
    }
}