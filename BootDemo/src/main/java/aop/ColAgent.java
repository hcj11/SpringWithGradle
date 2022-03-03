package aop;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Order(1)
@Slf4j
@Component
@Aspect
public class ColAgent {

    @Pointcut(value = "execution(void aop.UserService.*(..)) &&args(java.util.HashMap) ")
    public void pointcutForAddMap() {
    }

    @Pointcut(value = "execution(void aop.UserService.*(..)) &&args(aop.OrgCodeDomain,java.util.HashMap) ")
    public void pointcutForAddCol() {
    }

    @Around(value = "pointcutForAddMap()")
    public Object aroundForAddMap(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info(" invoke the method that name  is aroundForAddMap   ");
        return proceedingJoinPoint.proceed();
    }
    @Around(value = "pointcutForAddCol()")
    public Object aroundForAddCol(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        OrgCodeDomain name = (OrgCodeDomain) proceedingJoinPoint.getArgs()[0];
        HashMap arg = (HashMap) proceedingJoinPoint.getArgs()[1];
        log.info("==============hashMap:{}", arg);
        if (ObjectUtil.isEmpty(name) || ObjectUtil.isEmpty(name.getName())) {
            name = new OrgCodeDomain();
            name.setName("hellow--weihai");
        }
        log.info("after==========hello{}", name.getName());
        return proceedingJoinPoint.proceed(new Object[]{name, arg});
    }
}