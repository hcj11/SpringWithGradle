package mybatis;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.core.NamedThreadLocal;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
@EqualsAndHashCode(callSuper = false)
@Data
public class CustomRoutingDataSource extends AbstractRoutingDataSource implements AbstractCustomRoutingDataSource{
    /**
       目前是多线程不断变化参数，会导致线程安全问题，故采取threadLocal解决，
     */
    private AtomicInteger atomicInteger = new AtomicInteger(0);
//    NamedThreadLocal<Integer> route = new NamedThreadLocal<>("route");

    public CustomRoutingDataSource(Map<Object, Object> map) {
        Map<Object, Object> map1 = (Map<Object, Object>) map;
        setTargetDataSources(map1);
    }
    @Override
    protected DataSource determineTargetDataSource() {
        return super.determineTargetDataSource();
    }

    public void loadBalancePolicy(Integer index) {
        if (index == null) {
            int i = ThreadLocalRandom.current().nextInt(1, 4);
            logger.info(String.format("choose datasource:{%s}", i));
            atomicInteger.getAndSet(i);
        } else {
            logger.info(String.format("choose datasource:{%s}", index));
            atomicInteger.getAndSet(index);
        }
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return CurrentDataSource.getValWithIndex(atomicInteger.get());
    }


}