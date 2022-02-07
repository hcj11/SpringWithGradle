package dubbo;

import com.api.UserInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkImpl;
import org.apache.curator.retry.RetryNTimes;
import org.apache.dubbo.config.*;
import org.apache.dubbo.config.bootstrap.builders.ReferenceBuilder;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static utils.Utils.zookeeperHost;

@Slf4j
public class DubboConsumerTest {
    AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    CuratorFrameworkImpl curatorFramework = null;

    @BeforeEach
    public void setUp() throws Exception {
        String logger = System.setProperty("dubbo.application.logger", "slf4j");
        RetryNTimes retryNTimes = new RetryNTimes(2, 1000);
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .retryPolicy(retryNTimes).connectString("127.0.0.1:49609");
         curatorFramework = new CuratorFrameworkImpl(builder);
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
        }
    }

    @Test
    public void lookUpData() throws Exception {
        List<String> list = curatorFramework.getChildren().forPath("/dubbo/com.api.UserInterface/providers");
        list.stream().forEach(t->{   log.info("val:{}",t);});

        byte[] bytes = curatorFramework.getData().forPath("/dubbo");
        String s = new String(bytes);
        log.info("/dubbo:{}",s);
        curatorFramework.close();

    }
    @Test
    public void makeUp(){
        /**
         in case of the url or  ReferenceConfig
         */
        ReferenceConfig build = ReferenceBuilder.<UserInterface>newBuilder().protocol("")
                .build();

    }
    @Test
    public void startUpTest() throws InterruptedException {
        curatorFramework.close();

        ReferenceConfig<UserInterface> reference = new ReferenceConfig<>();

        reference.setApplication(new ApplicationConfig("first-dubbo-consumer"));//configManger.
        reference.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":49609"));
        reference.setInterface(UserInterface.class);

        UserInterface service = reference.get();
        String message = service.getName();
        System.out.println(message);
        log.info("name:{}",message);

    }
}
