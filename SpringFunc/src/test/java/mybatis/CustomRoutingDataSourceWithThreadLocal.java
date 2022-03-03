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
public class CustomRoutingDataSourceWithThreadLocal extends AbstractRoutingDataSource implements AbstractCustomRoutingDataSource {
    /**
       目前是多线程不断变化参数，会导致线程安全问题，故采取threadLocal解决，
     */
   public static NamedThreadLocal<Integer> route = new NamedThreadLocal<>("route");

    public CustomRoutingDataSourceWithThreadLocal(Map<Object, Object> map) {
        Map<Object, Object> map1 = (Map<Object, Object>) map;
        setTargetDataSources(map1);
    }
    @Override
    protected DataSource determineTargetDataSource() {
        return super.determineTargetDataSource();
    }

    public void loadBalancePolicy(Integer index) {
        // do nothing!!! in case of the thread var.
        if (index == null) {
            int i = ThreadLocalRandom.current().nextInt(1, 4);
            logger.info(String.format("choose datasource:{%s}", i));
            route.set(i);
        } else {
            logger.info(String.format("choose datasource:{%s}", index));
            route.set(index);
        }
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return CurrentDataSource.getValWithIndex(route.get());
    }
}