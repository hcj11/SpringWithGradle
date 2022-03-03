package context.aop;


import cn.hutool.core.util.ReflectUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cglib.core.ReflectUtils;

import java.lang.reflect.Method;
@Slf4j
public class TestInterceptor implements MethodInterceptor {

    private  int invocation = 0 ;

    public int getInvocation() {
        return invocation;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Method method = invocation.getMethod();
        String name = invocation.getMethod().getName();
        if (!name.equalsIgnoreCase("finalize")) {
            this.invocation++;
        }
        return  invocation.proceed();
    }

}
