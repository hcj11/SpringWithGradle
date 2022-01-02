package mybatis;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import sample.mybatis.dto.UserDto;
import sample.mybatis.mapper.MapperInterface;

import java.util.List;

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

    @Aspect
    public class Agent {
        @Pointcut(value = "execution(void sample.mybatis.mapper.MapperInterface.*(..)) && @annotation(sample.mybatis.annotation.SqlCheck) && @annotation(org.apache.ibatis.annotations.Insert)")
        public void pointForSqlCheck() {
        }
        @Pointcut(value = "execution(java.sql.PreparedStatement com.mockrunner.mock.jdbc.MockConnection.prepareStatement(..)) && args( sql,  resultSetType,  resultSetConcurrency)")
        public void prepareStatement(String sql, int resultSetType, int resultSetConcurrency) {
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
