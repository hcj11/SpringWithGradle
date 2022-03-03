package mybatis.transaction;

import context.CompontScan;
import lombok.extern.slf4j.Slf4j;
import mybatis.AopConfig;
import mybatis.CustomRoutingDataSource;
import mybatis.CustomRoutingDataSourceWithThreadLocal;
import mybatis.MybatisBean;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;
import sample.mybatis.mapper.MapperInterface;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Slf4j
public class MybatisBeanForRouting extends MybatisBeanForPG {
    /**
     todo data need reset  by language driver.
     and add second before method
     */
    @BeforeEach
    public void setUp(){
        log.info("====================================MybatisBeanForRouting initilize...");
    }

    @Test
    public void outerTestWithAop(){
        /**
         * aop代理，或者 mybatis插件，方式进行注入，
         */
        /**
         * 减少方法传递，requestBean , get 进行方法拦截，
         */
//        AopConfig();
    }
    /**
     * and BaseExecutor use  SpringManagedTransaction to openConnection ,
     * so Spring association with BaseExecutor.
     */
    @Test
    @Disabled
    public void mysqlForRouteTest(){
        // register(AopConfig.class)
        /**
         Caused by: java.sql.SQLSyntaxErrorException:
         You have an error in your SQL syntax;
         check the manual that corresponds to your
         MySQL server version for the right syntax to use near
         '"public"."users"("id", "user_group_id", "name", "age", "create_time", "money", "' at line 1
         */
        Assertions.assertThrows(java.sql.SQLSyntaxErrorException.class,()->{
            Object[] objects = transactionTestForNew(2);
        });
    }
    /**
     *  RouteDataSourceToTransformWithThreadLocal=atomic
     */
    @Test
    public void PgAndMysqlToTransformWithRouteDataSourceWithoutThreadLocalAndPg(){
        applicationContext.register(AopConfig.class);
        /**
         * default is pg
         */
        Object[] objects = transactionTestForNew(1);
        doAction(objects);

    }
    @Test
    public void PgAndMysqlToTransformWithRouteDataSourceWithoutThreadLocalAndMysql(){
        applicationContext.register(AopConfig.class);
        Object[] objects = transactionTestForNew(2);
        doAction(objects);
    }
    /**
     RouteDataSourceToTransformWithThreadLocal=threadLocal
     */
    @Test
    public void PgAndMysqlToTransformWithRouteDataSourceWithThreadLocalAndPg(){
        applicationContext.register(AopConfig.class);
        Object[] objects = transactionTestForNew(1);
        doAction(objects);
    }
    @Test
    public void PgAndMysqlToTransformWithRouteDataSourceWithThreadLocalAndMysql(){
        /**
         * use the mysql. truncat throws exception
         */
        applicationContext.register(AopConfig.class);
        Object[] objects = transactionTestForNew(2);
        doAction(objects);
    }

    private void doAction(Object[] objects){
        extractTestMethodWithoutTransaction(objects);
//        extractTestMethodWithTransaction(objects);
    }
    @Test
    public void PgForRouteTest(){
        applicationContext.register(AopConfig.class);
        Object[] objects = transactionTestForNew(1);
        extractTestMethodWithTransaction(objects);
    }

    private void extractTestMethodWithoutTransaction(Object[] objects){
        MapperInterface mapper = (MapperInterface)objects[0];
        boolean someThings = mapper.findSomeThings();
        Assert.assertFalse(someThings);
        Assertions.assertFalse(mapper.findSomeThingsForMysql());;
    }
    private void extractTestMethodWithTransaction(Object[] objects){
        MapperInterface mapper = (MapperInterface)objects[0];
        TransactionTemplate transactionTemplate = (TransactionTemplate)objects[1]; // ab
        transactionTemplate.setPropagationBehavior(0);
/**
 transactionTemplate
 in case of the transaction bound datasource ensure the connection and use after start transaction
 can change datasource  after  transaction commit
 so, 更新数据源的场景可以说 批处理操作， 或者 xa系统上、分布式系统上，
 而开启事务 可以用到 实时系统上、业务系统上，
 */
        transactionTemplate.executeWithoutResult((TransactionStatus status)->{
            boolean someThings = mapper.findSomeThings();
            Assert.assertFalse(someThings);
            Assertions.assertFalse(mapper.findSomeThingsForMysql());;
        });
    }


}
