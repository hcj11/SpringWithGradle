package dubbo.demo;

import com.api.UserInterface;
import com.provider.ProviderBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkImpl;
import org.apache.curator.retry.RetryNTimes;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;
import utils.Utils;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static utils.Utils.zookeeperHost;


@Slf4j
public class DubboProviderTest {

    public void checkLog4j2() {


        log.info("current thread class loader{}", Thread.currentThread().getContextClassLoader());

//        InputStream resourceAsStream = DubboProviderTest.class.getResourceAsStream("/log4j2.xml");
//        Assert.notNull(resourceAsStream, "not found this ");
    }

    @BeforeEach
    public void setUp() throws Exception {
        String logger = System.setProperty("dubbo.application.logger", "slf4j");
        checkLog4j2();

        RetryNTimes retryNTimes = new RetryNTimes(2, 1000);
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .retryPolicy(retryNTimes).connectString("127.0.0.1:51464");
        CuratorFrameworkImpl curatorFramework = new CuratorFrameworkImpl(builder);
        curatorFramework.start();

        AtomicBoolean flag = new AtomicBoolean(false);

        curatorFramework.blockUntilConnected();

        curatorFramework.getChildren().forPath("/").forEach(name -> {
            if (name.equals("dubbo")) {
                flag.set(true);
            }
            log.info("child-name:{}", name);
        });
        if (!flag.get()) {
            String s = curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/dubbo");
            log.info("operation-res:{}", s);
        }else{
            Stat stat = curatorFramework.checkExists().forPath("/dubbo");
            log.info("current-stat:{}", stat);
            curatorFramework.close();
        }




    }

    @Test
    public void startUpTest() throws InterruptedException {

        ServiceConfig<UserInterface> service = new ServiceConfig<>();
        //export the url   method..

        service.setApplication(new ApplicationConfig("first-dubbo-provider"));
        service.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":51239"));

        service.setInterface(UserInterface.class);
        service.setRef(new ProviderBean.UserInterfaceImpl());
        service.export();

        System.out.println("dubbo service started");
        new CountDownLatch(1).await();

    }
}
