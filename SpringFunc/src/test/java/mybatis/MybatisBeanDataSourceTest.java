package mybatis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.jdbc.datasource.lookup.IsolationLevelDataSourceRouter;

import javax.sql.DataSource;


@Slf4j
public class MybatisBeanDataSourceTest extends MybatisBean {
    public Object makeUpContext(Class cls){
        applicationContext.register(RouteDataSourceConfigurationDemo.class);
        startContext();
        Object bean = applicationContext.getBean(cls);
        return bean;
    }
    /**
     * and add aop to deal with the
     */
    @Test
    public void try1(){

    }
    @Test
    public void determinedDataSourceWithSpecialTest(){
        CustomRoutingDataSource customRoutingDataSource = (CustomRoutingDataSource) makeUpContext(CustomRoutingDataSource.class);
        customRoutingDataSource.loadBalancePolicy(null);

        DataSource dataSource = customRoutingDataSource.determineTargetDataSource();
        log.info("{}",dataSource);
    }
    @Test
    public void determinedDataSourceTest(){

        CustomRoutingDataSource customRoutingDataSource = (CustomRoutingDataSource) makeUpContext(CustomRoutingDataSource.class);
        DataSource dataSource = customRoutingDataSource.determineTargetDataSource();
        log.info("{}",dataSource);
        // autowird the dataSource  for jdbc  or mybatis.

    }
}
