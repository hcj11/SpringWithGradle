package mybatis.transaction;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import context.CompontScan;
import lombok.extern.slf4j.Slf4j;
import mybatis.CustomRoutingDataSource;
import mybatis.MybatisBean;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
public class MybatisBeanForPG extends MybatisBean {
    /**
     todo data need rest  by language driver.
     and add second before method
     */
    @BeforeEach
    public void setUp(){
        log.info("====================================MybatisBeanForPG initilize...");
    }
    @Test
    public void printBean(){
        super.startContext();
        PlatformTransactionManager bean = applicationContext.getBean(PlatformTransactionManager.class);
        org.junit.Assert.assertNotNull(bean);
        CompontScan.printSingleton(applicationContext);
    }
    @Test
    public void transcationInitTest(){
        transactionTestForNew();

    }
    public void runScript(DataSource customRoutingDataSource,String script) throws SQLException, IOException {
        Connection connection = customRoutingDataSource.getConnection();
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setAutoCommit(true);
        scriptRunner.setStopOnError(true);

        PrintStream out = System.out;
        PrintWriter printWriter = new PrintWriter(out);

        scriptRunner.setLogWriter(printWriter);
        scriptRunner.setThrowWarning(false);

        Reader resourceAsReader = Resources.getResourceAsReader(script);
        scriptRunner.runScript(resourceAsReader);
    }
    public void tableCreateTest(MapperInterface mapperInterface, DataSource dataSource) throws SQLException, IOException {
        String script= "create.sql";
        CustomRoutingDataSource customRoutingDataSource = (CustomRoutingDataSource) dataSource;
        customRoutingDataSource.loadBalancePolicy(1);
        runScript(dataSource,script);
    }
    public void dataReset(MapperInterface mapperInterface, DataSource dataSource) throws SQLException, IOException {

        mapperInterface.truncate();
        String script= "insert.sql";

        CustomRoutingDataSource customRoutingDataSource = (CustomRoutingDataSource) dataSource;
        customRoutingDataSource.loadBalancePolicy(1);
        runScript(customRoutingDataSource,script);


    }
    /**
     1.test outer update success
     2.test inner update fail.

     1.test outer update fail
     2.test inner update success.
     */
    public void makeDataSourceAndMapper(){

    }
    public Object[] transactionTestForNew(){
        super.startContext();
        DataSourceTransactionManager bean = applicationContext.getBean(DataSourceTransactionManager.class);
        org.junit.Assert.assertNotNull(bean);
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        MapperInterface mapper = sqlSession.getMapper(MapperInterface.class);
        try {
            dataReset(mapper,bean.getDataSource());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TransactionTemplate transactionTemplate = new TransactionTemplate(bean);
        TransactionTemplate transactionTemplate2 = new TransactionTemplate(bean);
        return new Object[]{mapper,transactionTemplate,transactionTemplate2};
    }
    @Test
    public void OuterSuccessAndInnerFail(){
        Object[] objects = transactionTestForNew();
        MapperInterface mapper = (MapperInterface)objects[0];
        TransactionTemplate transactionTemplate = (TransactionTemplate)objects[1];
        TransactionTemplate transactionTemplate2 = (TransactionTemplate)objects[2];

        transactionTemplate2.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        try {
            transactionTemplate.executeWithoutResult((TransactionStatus transactionStatus)->{
                mapper.outerUpdate(1);
                for (int i=1;i<=3;i++){
                    int finalI = i;
                    transactionTemplate2.executeWithoutResult((TransactionStatus transactionStatus2)->{
                        // throws the Caused by: org.postgresql.util.PSQLException: 错误: 重复键违反唯一约束"idx_name"
                        // 两个事务互不干扰。
                        mapper.nestUpdate(finalI);
                    });
                }

            });
        }catch (Exception e){
            e.printStackTrace();
        }

        Assert.assertTrue(mapper.findSomeThings());;
        Assert.assertEquals(mapper.findAnotherThings().toArray(),new Boolean[]{true,true});;

    }
    @Test
    public void OuterFailAndInnerSuccess(){
        Object[] objects = transactionTestForNew();
        MapperInterface mapper = (MapperInterface)objects[0];
        TransactionTemplate transactionTemplate = (TransactionTemplate)objects[1];
        TransactionTemplate transactionTemplate2 = (TransactionTemplate)objects[2];
        transactionTemplate2.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transactionTemplate.executeWithoutResult((TransactionStatus transactionStatus)->{
            for (int i=1;i<=3;i++){
                int finalI = i;
                // throws the Caused by: org.postgresql.util.PSQLException: 错误: 重复键违反唯一约束"idx_name"
                // and the inner don't run.
                mapper.outerUpdate(finalI);
            }

            transactionTemplate2.executeWithoutResult((TransactionStatus transactionStatus2)->{
                mapper.nestUpdate(1);
            });
        });
    }
    /**
     更新
     */
    @Test
    public void transactionTestForNest(){
        Object[] objects = transactionTestForNew();
        MapperInterface mapper = (MapperInterface)objects[0];
        TransactionTemplate transactionTemplate = (TransactionTemplate)objects[1];
        TransactionTemplate transactionTemplate2 = (TransactionTemplate)objects[2];

        transactionTemplate2.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
        try {
            transactionTemplate.executeWithoutResult((TransactionStatus transactionStatus)->{
                mapper.outerUpdate(1);
                for (int i=1;i<=3;i++){
                    int finalI = i;
                    transactionTemplate2.executeWithoutResult((TransactionStatus transactionStatus2)->{
                        // throws the Caused by: org.postgresql.util.PSQLException: 错误: 重复键违反唯一约束"idx_name"
                        // 部分保存成功。
                        mapper.nestUpdate(finalI);
                    });
                }

            });
        }catch (Exception e){
            e.printStackTrace();;
        }
        Assert.assertTrue(mapper.findSomeThings());;
        Assert.assertEquals(mapper.findAnotherThings().toArray(),new Boolean[]{true,true});;

    }

    /**
     *
     */
    @Test
    public void try1() {
        super.startContext();
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        MapperInterface mapper = sqlSession.getMapper(MapperInterface.class);
        List<Map<String, String>> maps = mapper.queryInPg();
        maps.stream().forEach(( kv) -> {
            log.info("map:{}", kv.toString());
        });

        List<Map<String, String>> maps2 = mapper.queryJsonBInPg(1);
// use ?? to escape ?.
        maps2.stream().forEach(( kv) -> {
            log.info("map2:{}", kv.toString());
        });


    }
}
