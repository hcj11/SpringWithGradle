package Quartz;

import Utils.Utils.Constants;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.UUID;

@EnableWebMvc
@SpringBootApplication
public class Config {

    @Qualifier(value = "SchedulerFactory")
    @Autowired
    private Scheduler scheduler;


    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Config.class, args);
        Config bean = run.getBean(Config.class);
        bean.send();
    }

    public void send(){
        MessageDto jobEntity = new MessageDto();
        jobEntity.setCronExpression("10 53 11 * * ?");
        jobEntity.setStatus(Constants.ScheduleStatus.NORMAL.getValue());
        jobEntity.setJobId(UUID.randomUUID().toString().replaceAll("-",""));
        ScheduleUtils.createScheduleJob(scheduler,jobEntity);
        ScheduleUtils.run(scheduler,jobEntity);

    }

}
