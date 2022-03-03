package dubbo;

import com.api.*;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.spring.context.annotation.DubboConfigConfiguration;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import utils.Utils;
/**
 * fixme:
 * and try to add new interface for test , 不用，monitor 监控表征一个时间段的接口执行情况。
 * then try new dubboRegistry
 */
/**
 start the service
 */
@SpringBootTest(classes = {SpringBootTestForDubbo.Dummy.class})
public class SpringBootTestForDubbo {
    @Autowired
    private AnnotationConfigApplicationContext applicationContext;
    /**
     * can try update the env. and don't use the zkregistry .
     */
    @Reference(url = "dubbo://localhost:12346?version=2.5.7",lazy = true)
    public DemoInterface demoInterface;
// fixme: dubbo注册有问题，未查询到。问题不大，
//    @Reference(version = "2.5.7",registry = {"dubboProtocol1"},lazy = true)
//    public DemoInterfaceWithDubboRegistry demoInterfaceWithDubboRegistry;

    @Reference(registry = {"zk1"},lazy = true,version = "2.5.7",protocol = "dubbo")
    public UserInterface userInterfaceWithZk;
// fixme: 未实现
//    @Reference(registry = {"zk1"},lazy = true,version = "2.5.7",protocol = "http")
//    public HttpInterface userInterfaceWithZkAndHttpProtocol;

    @Reference(registry = {"zk1"},lazy = true,version = "2.5.7",protocol = "rmi")
    public RmiInterface userInterfaceWithZkAndRmiProtocol;

    @Configuration
    @EnableAutoConfiguration
    public static class Dummy{
    }        Object lock = new Object();
    @Test
    public void startUpWithDubbo() throws InterruptedException {
//        Assertions.assertEquals(demoInterfaceWithDubboRegistry.doActionForDemo(),"doActionForDemo" );;
    }
    /**
     * how to get the registry  bind
     */
    @Test
    public void startUpWithZk() throws InterruptedException {
        Assertions.assertEquals(userInterfaceWithZkAndRmiProtocol.doActionForRmi(),"doActionForRmi" );;
        Assertions.assertEquals(userInterfaceWithZkAndRmiProtocol.doActionForRmi(),"doActionForRmi" );;
        Assertions.assertEquals(userInterfaceWithZkAndRmiProtocol.doActionForRmi(),"doActionForRmi" );;
        Assertions.assertEquals(userInterfaceWithZk.getName(),"hcjForNewDubbo" );;
        Assertions.assertEquals(demoInterface.doActionForDemo(),"doActionForDemoWithzk" );;
        synchronized (lock){lock.wait();}

    }
    @Test
    public void startUp() throws InterruptedException {

        Utils.print(applicationContext);
        Assertions.assertEquals(demoInterface.doActionForDemo(),"doActionForDemoWithzk" );;
        synchronized (lock){lock.wait();}
    }

}
