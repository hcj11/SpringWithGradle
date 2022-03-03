package mybatis;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import sample.mybatis.annotation.EnableMysql;
import sample.mybatis.annotation.EnablePg;
import sample.mybatis.dto.UserDto;
import sample.mybatis.mapper.MapperInterface;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@EnableAspectJAutoProxy(proxyTargetClass = false)
@Configuration
public class AopConfig {
    @Bean
    public Agent agent() {
        return new Agent();
    }

    @Bean
    public void copyMarker(){

    }
    @Autowired
    private DataSource dataSource;

    public AopConfig(DataSource dataSource){
        this.dataSource=dataSource;
    }
    @Component
    @Aspect
    public class Agent {
        /**
         * how to 确认位置， sample.mybatis.annotation
         * && (@annotation(sample.mybatis.annotation.EnableMysql)||@annotation(sample.mybatis.annotation.EnablePg))
         */
        @Pointcut(value = "execution(* sample.mybatis.mapper.MapperInterface.*(..)) && (@annotation(sample.mybatis.annotation.EnableMysql)||@annotation(sample.mybatis.annotation.EnablePg))")
        public void pointForDataSourceRoute(){
        }
        @Pointcut(value = "execution(void sample.mybatis.mapper.MapperInterface.*(..)) && @annotation(sample.mybatis.annotation.SqlCheck) && @annotation(org.apache.ibatis.annotations.Insert)")
        public void pointForSqlCheck() {
        }
        @Pointcut(value = "execution(java.sql.PreparedStatement com.mockrunner.mock.jdbc.MockConnection.prepareStatement(..)) && args( sql,  resultSetType,  resultSetConcurrency)")
        public void prepareStatement(String sql, int resultSetType, int resultSetConcurrency) {
        }

        @Around(value = "pointForDataSourceRoute()")
        public Object beforeExecuteSqlForDataSourceRoute(ProceedingJoinPoint proceedingJoinPoint) throws NoSuchMethodException {
            /**
             CustomRoutingDataSourceWithThreadLocal vs CustomRoutingDataSource
             判断注解类型，  先去取出来method，执行的方法
             */
            Object[] args = proceedingJoinPoint.getArgs();
            Class[] classes = Arrays.stream(args).map(arg -> {
                return arg.getClass();
            }).collect(Collectors.toList()).toArray(new Class[0]);

            MethodInvocationProceedingJoinPoint methodInvocation = (MethodInvocationProceedingJoinPoint) proceedingJoinPoint;
            Signature signature = methodInvocation.getSignature();
            String name = signature.getName();
            Class declaringType = signature.getDeclaringType();
            Method method = declaringType.getMethod(name, classes);

            AbstractCustomRoutingDataSource dataSource = (AbstractCustomRoutingDataSource) AopConfig.this.dataSource;
            if(method.isAnnotationPresent(EnableMysql.class)){
                dataSource.loadBalancePolicy(2);
            }else if(method.isAnnotationPresent(EnablePg.class)){
                dataSource.loadBalancePolicy(1);
            }

            try {
                return proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return null;
        }

        @Before(value = "prepareStatement( sql,  resultSetType,  resultSetConcurrency)")
        public void beforeExecuteSql(String sql, int resultSetType, int resultSetConcurrency) {
            log.info("execute sql : {}", sql);
        }

        @Around(value = "pointForSqlCheck()")
        public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
            List<UserDto> args = (List<UserDto>) proceedingJoinPoint.getArgs()[0];
            MapperInterface target = (MapperInterface) proceedingJoinPoint.getTarget();
            if (ObjectUtil.isNotEmpty(args) && args.size() > 4) {
                List<List<UserDto>> partition = Lists.partition(args, 4);
                partition.stream().forEach(userDtos -> {
                    userDtos.stream().forEach(userDto -> userDto.setOrgCode("111"));
                    target.insertList(userDtos);
                });
                return null;
            } else {
                Object proceed = proceedingJoinPoint.proceed();
                return proceed;
            }

        }
    }
}
