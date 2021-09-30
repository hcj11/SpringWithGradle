package context.async;

import context.aop.AutoProxyCreatorTest;
import io.netty.util.concurrent.ThreadPerTaskExecutor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;


@Configuration
@EnableAsync
@Data
@Slf4j
public class AsyncResult {
    /**
     * 解耦
     */
    @Qualifier("e1")
    @Async
    public void hello(String... helloArgs){
        log.info("{},=============say hello================",Thread.currentThread().getName());
    }

    @Bean("e1")
    public Executor executorService(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        return threadPoolTaskExecutor;
    }

    public AsyncUncaughtExceptionHandler exceptionHandler(){
        AsyncUncaughtExceptionHandler asyncUncaughtExceptionHandler = new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                log.error("{}",ex.getMessage());
            }
        };
        return asyncUncaughtExceptionHandler;
    }

    @Test
    public void test1() throws Exception {
        /**
         * 构建代理，对参数进行拦截， aspect + proxyFactory
         * 提前对参数，进行过滤。
         */

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                AsyncResult.class,CutomProxyBean.class);
        /**
         * get  single instance
         */
        AsyncResult bean = (AsyncResult)context.getBean("proxyFactoryBean");
        bean.hello("one","2","three");

        Thread.sleep(100000);

    }
}
