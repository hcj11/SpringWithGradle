package Quartz;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ��ʱ��������
 *
 * @author Mark sunlightcs@gmail.com
 */
@Configuration
public class ScheduleConfig {
    @Autowired
    private Environment environment;

    @Bean("druidDataSource")
    public DataSource dataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("root");
        druidDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/dataq?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai");
        return druidDataSource;
    }

    @Qualifier("druidDataSource")
    @Autowired
    private DataSource dataSource;


    @Bean(name="SchedulerFactory")
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        //quartz����
        Properties prop = new Properties();
        prop.put("org.quartz.scheduler.instanceName", "CESScheduler");
        prop.put("org.quartz.scheduler.instanceId", "AUTO");
        //�̳߳�����
        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        prop.put("org.quartz.threadPool.threadCount", "20");
        prop.put("org.quartz.threadPool.threadPriority", "5");
        //JobStore����
        prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        //��Ⱥ����
        prop.put("org.quartz.jobStore.isClustered", "true");
        prop.put("org.quartz.jobStore.clusterCheckinInterval", "15000");
        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");

        prop.put("org.quartz.jobStore.misfireThreshold", "12000");
        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
        prop.put("org.quartz.jobStore.selectWithLockSQL", "SELECT * FROM {0}LOCKS UPDLOCK WHERE LOCK_NAME = ?");

        //PostgreSQL���ݿ⣬��Ҫ�򿪴�ע��
        //prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");

        factory.setQuartzProperties(prop);
        factory.setDataSource(dataSource);
        factory.setSchedulerName("CESScheduler");
        //��ʱ����
        factory.setStartupDelay(30);
        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
        //��ѡ��QuartzScheduler ����ʱ���¼����ڵ�Job�������Ͳ���ÿ���޸�targetObject��ɾ��qrtz_job_details���Ӧ��¼��
        factory.setOverwriteExistingJobs(true);
        //�����Զ�������Ĭ��Ϊtrue
        factory.setAutoStartup(true);

        return factory;
    }
}