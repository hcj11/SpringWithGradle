package dubbo.spring;

import com.provider.NewUserInterfaceImpl;
import config.DubboConsumerConfig;
import config.DubboProviderConfig;
import org.apache.dubbo.config.spring.ServiceBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import utils.Utils;

import java.util.concurrent.*;

public class SpringUnionTest {
    static {
        System.setProperty("CONFIG_FILE_PROPERTY","logback-test.xml");

    }
    @Test
    public void testStartTheProvider(){
        /**
         * start the server ,and then reference to connect .
         */
    }
    @Test
    public void test(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DubboProviderConfig.class);
        ServiceBean serviceBean = context.getBean("ServiceBean:com.api.UserInterface", org.apache.dubbo.config.spring.ServiceBean.class);
        Object ref = serviceBean.getRef();
        Assertions.assertNull(serviceBean.getService());
        NewUserInterfaceImpl bean = context.getBean(NewUserInterfaceImpl.class);
        Assertions.assertEquals(ref,bean);
    }
    @Test
    public void scanDubboBeanForConsumer() throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext( DubboConsumerConfig.class);
        Utils.print(context);
    }
    @Test
    public void scanDubboBean() throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DubboProviderConfig.class, DubboConsumerConfig.class);
        Utils.print(context);
        DubboProviderConfig.countDownLatch.await();;
        DubboConsumerConfig consumerConfig = context.getBean(DubboConsumerConfig.class);
        Assertions.assertEquals(consumerConfig.getName(),"hcjForNewDubbo" );

    }
}
