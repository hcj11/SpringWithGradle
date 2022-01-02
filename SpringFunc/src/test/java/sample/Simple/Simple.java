package sample.Simple;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.func.Func1;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.quartz.CronTrigger;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import sample.mybatis.domain.User;

import java.util.regex.Pattern;
@Slf4j
public class Simple {
    @org.junit.jupiter.api.Test
    public void lambdaResolveTest(){
        lambdaResolve();;
    }
    public static void lambdaResolve(){
        SFunction<User, String> getName = User::getName;
        SerializedLambda lambda = SerializedLambda.resolve(new User().getSFunction());
        String implMethodName = lambda.getImplMethodName();
        log.info("{}",implMethodName);

        SerializedLambda lambda1 = SerializedLambda.resolve(getName);
        String implMethodName1 = lambda1.getImplMethodName();
        log.info("{}",implMethodName1);

    }

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
