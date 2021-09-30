package Quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.Assert;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Utils.Constants.JOB_PARAM_KEY;


/**
 * 定时任务
 *
 * @author Mark sunlightcs@gmail.com
 */
@Slf4j
@DisallowConcurrentExecution
public class ScheduleJob extends QuartzJobBean {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private ExecutorService service = Executors.newSingleThreadExecutor();

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Object o1 = jobDataMap.get(JOB_PARAM_KEY);
        Assert.isTrue(o1 != null, "不能为空");
        System.out.println(o1);
        // ScheduleJobEntity(jobId=5, beanName=createStatsJob, params=, cronExpression=0 0 19 * * ?, status=0, remark=定时生成检测任务, createTime=Fri Dec 27 09:56:23 CST 2019)
        String substring = o1.toString();
        ClassLoader classLoader = o1.getClass().getClassLoader();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        if (o1 instanceof MessageDto) {
            logger.info("yes , 同一个实例");
        }
        Assert.isTrue(classLoader!=contextClassLoader);

    }
}