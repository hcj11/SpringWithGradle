package Simple;

import cn.hutool.core.lang.Assert;
import org.junit.Test;
import org.quartz.CronTrigger;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

import java.util.regex.Pattern;

public class Simple {

    @Test
    public void try1(){
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        CronTrigger object = cronTriggerFactoryBean.getObject();
        object.getCronExpression();
        CronTrigger bu = object.getTriggerBuilder().build();




        Pattern compile = Pattern.compile("20[0-9]{18}");
        boolean b = compile.matcher("2011111111111111111d").find();
        Assert.isFalse(b);
        boolean b1 = compile.matcher("20111111111111111112").find();
        Assert.isTrue(b1);
    }
}
