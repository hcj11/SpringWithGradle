package context.aop;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * now 1. parentClass
 * 2. Interface
 */
interface CustomInterface{
    public void doAction();
}
@Data
@Slf4j
class CustomSubClass implements CustomInterface{
    @Override
    public void doAction() {
        log.info("invoke the method: doAction");
    }
}
@Slf4j
class CustomMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        log.error("CustomMethodInterceptor proxy the method:{}",method.getName());
        return proxy.invokeSuper(obj,args);
    }
}
/**
 */
@Slf4j
public class CglibTest {
    CustomSubClass customSubClass =null;
    @BeforeEach
    public void setUp(){
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new CustomMethodInterceptor());
        enhancer.setSuperclass(CustomSubClass.class);
        enhancer.setInterceptDuringConstruction(true);
        customSubClass = (CustomSubClass)enhancer.create();
        log.info("proxy: CustomSubClass:{}",customSubClass.toString());
    }
    @Test
    public void enHancerMakeUpTest(){
        customSubClass.doAction();

    }
}
