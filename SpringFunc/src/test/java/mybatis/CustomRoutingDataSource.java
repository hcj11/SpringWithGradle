package mybatis;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
@EqualsAndHashCode(callSuper = false)
@Data
public class CustomRoutingDataSource extends AbstractRoutingDataSource {
    private AtomicInteger atomicInteger = new AtomicInteger(0);
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
            atomicInteger.set(i);
        } else {
            logger.info(String.format("choose datasource:{%s}", index));
            atomicInteger.set(index);
        }
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return CurrentDataSource.getValWithIndex(atomicInteger.get());
    }
}