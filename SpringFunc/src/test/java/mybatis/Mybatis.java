package mybatis;
/**
 * spring-mybatis-boot
 */

import cn.hutool.core.lang.Assert;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.github.pagehelper.PageInterceptor;
import com.mockrunner.mock.jdbc.*;
import context.CompontScan;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import mybatis.interceptor.AddOrgCodeForPageInteceptor;
import mybatis.transaction.MybatisForPG;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sample.mybatis.mapper.MapperInterface;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static mybatis.Mybatis.TestTypes.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;

@Configuration
@ImportAutoConfiguration(MybatisAutoConfiguration.class)
class MybatisAutoConfigurationDemo {

}
@Configuration
@ImportAutoConfiguration(value = {TransactionAutoConfiguration.class,DataSourceTransactionManagerAutoConfiguration.class})
class TransactionAutoConfigurationDemo {
}

@Configuration
@ImportAutoConfiguration(value = {MybatisPlusAutoConfiguration.class})
class MybatisPlusConfigurationDemo {
    @Bean
    public CustomIdGenerateMetaObjectHandler customIdGenerateMetaObjectHandler(){
        return new CustomIdGenerateMetaObjectHandler();
    }
}

class CustomIdGenerateMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("id", UUID.randomUUID().toString());
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
@Slf4j
public class Mybatis {
    /**
     the unit test type of  sqlsessionFactory
     */
    enum TestTypes{
        PG,PLUS,EMBED;
        public void doAction(AnnotationConfigApplicationContext applicationContext) throws SQLException {
            if(this==PG){
                setupSqlSessionFactoryForPG(applicationContext);
            }else if(this==EMBED){
                setupSqlSessionFactory(applicationContext);
            }else{
                /**
                 org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
                 configuration.setDefaultResultSetType(ResultSetType.FORWARD_ONLY);
                 definition.getPropertyValues().add("configuration",configuration);
                 */
//                changeQualifier(applicationContext);
            }
        }
    }

    @PropertySource(value = "classpath:/application.yml")
    @Data
    @Configuration(proxyBeanMethods = false)
    @MapperScan(basePackages = {"sample.mybatis.mapper", "sample.mybatis.domain"})
    public static class MapperConfiguration  implements BeanFactoryPostProcessor {

        @Qualifier("mapperInterface")
        @Autowired
        private MapperInterface mapperInterface1;

        @Resource
        private MapperInterface mapperInterface2;
        @Bean
        public ConfigurationCustomizer configurationCustomizer(){
           return  (org.apache.ibatis.session.Configuration con)->{
               if(con.getClass().isAssignableFrom(MybatisConfiguration.class) ){
                   MybatisConfiguration mybatisConfiguration =  (MybatisConfiguration)con;
                   mybatisConfiguration.setDefaultResultSetType(ResultSetType.FORWARD_ONLY);
               }
           };
        }

        @Bean
        public static Interceptor AddOrgCodeForPageInteceptor() {
            AddOrgCodeForPageInteceptor addOrgCodeInteceptor = new AddOrgCodeForPageInteceptor();
            addOrgCodeInteceptor.setProperties(new Properties());
            return addOrgCodeInteceptor;
        }

        @Bean
        public static Interceptor PageInterceptor() {
            PageInterceptor pageInterceptor = new PageInterceptor();

            pageInterceptor.setProperties(new Properties());
            return pageInterceptor;
        }

        @Primary
        @Bean
        public static DruidDataSource pgDruidDataSource() {
            DruidDataSource ds = new DruidDataSource();
            ds.setDriverClassName("org.postgresql.Driver");
            ds.setUrl(// Postgresql
                    "jdbc:postgresql://localhost:5434/postgres?characterEncoding=utf8&useSSL=true&serverTimezone=Asia" +
                            "/Shanghai");
            ds.setUsername("postgres");
            ds.setPassword("postgres");
            ds.setInitialSize(5);
            return ds;
        }

        @Bean
        public static DruidDataSource druidDataSource() {
            DruidDataSource ds = new DruidDataSource();
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
            ds.setUrl(
                    "jdbc:mysql://localhost:3306/test?characterEncoding=utf8&useSSL=true&serverTimezone=Asia" +
                            "/Shanghai");
            ds.setUsername("root");
            ds.setPassword("root");
            ds.setInitialSize(5);
            return ds;
        }

        public static MockPreparedStatement metaExtractForVoid() throws SQLException {
            MockPreparedStatement mockPreparedStatement = mock(MockPreparedStatement.class);
            given(mockPreparedStatement.getResultSet()).willReturn(null);
            return mockPreparedStatement;
        }

        public static MockPreparedStatement metaExtractForMap() throws SQLException {

            MockPreparedStatement mockPreparedStatement = mock(MockPreparedStatement.class);
            MockResultSet resultSetMock = mock(MockResultSet.class);
            MockResultSetMetaData mockResultSetMetaData = new MockResultSetMetaData();
            mockResultSetMetaData.setColumnCount(2);

            mockResultSetMetaData.setColumnLabel(1, "key");
            mockResultSetMetaData.setColumnLabel(2, "val");

            mockResultSetMetaData.setColumnName(1, "key");
            mockResultSetMetaData.setColumnName(2, "val");
            mockResultSetMetaData.setColumnType(1, Types.VARCHAR);
            mockResultSetMetaData.setColumnType(2, Types.VARCHAR);

            mockResultSetMetaData.setColumnClassName(1, String.class.getName());
            mockResultSetMetaData.setColumnClassName(2, String.class.getName());
            given(resultSetMock.getMetaData()).willReturn(mockResultSetMetaData);

            given(resultSetMock.getString("key")).willReturn("name", "name1", "name2", "name3");
            given(resultSetMock.getString("val")).willReturn("hcj", "hcj1", "hcj2", "hcj3");

            given(resultSetMock.next()).willReturn(true, true, true, true, false);
            given(mockPreparedStatement.getResultSet()).willReturn(resultSetMock);
            return mockPreparedStatement;
        }

        public static MockPreparedStatement metaExtractForList() throws SQLException {
            MockPreparedStatement mockPreparedStatement = mock(MockPreparedStatement.class);
            MockResultSet resultSetMock = mock(MockResultSet.class);

            MockResultSetMetaData mockResultSetMetaData = new MockResultSetMetaData();
            mockResultSetMetaData.setColumnCount(1);
            mockResultSetMetaData.setColumnLabel(1, "list");
            mockResultSetMetaData.setColumnName(1, "list");
            mockResultSetMetaData.setColumnType(1, Types.VARCHAR);
            mockResultSetMetaData.setColumnClassName(1, String.class.getName());
            given(resultSetMock.getMetaData()).willReturn(mockResultSetMetaData);
            given(resultSetMock.getString("list")).willReturn("hello world", "tomorrow", "firm");
            given(resultSetMock.next()).willReturn(true, false);
            given(mockPreparedStatement.getResultSet()).willReturn(resultSetMock);
            return mockPreparedStatement;
        }

        public static MockPreparedStatement metaExtractForPage() throws SQLException {
            MockPreparedStatement mockPreparedStatement = mock(MockPreparedStatement.class);
            MockResultSet resultSetMock = mock(MockResultSet.class);
            MockResultSetMetaData mockResultSetMetaData = new MockResultSetMetaData();
            mockResultSetMetaData.setColumnCount(1);
            mockResultSetMetaData.setColumnLabel(1, "list");
            mockResultSetMetaData.setColumnName(1, "list");
            mockResultSetMetaData.setColumnType(1, Types.VARCHAR);
            mockResultSetMetaData.setColumnClassName(1, String.class.getName());
            given(resultSetMock.getMetaData()).willReturn(mockResultSetMetaData);
            given(resultSetMock.getString("list")).willReturn("hello world", "tomorrow", "firm");
            given(resultSetMock.next()).willReturn(true, false);
            given(mockPreparedStatement.getResultSet()).willReturn(resultSetMock);
            return mockPreparedStatement;
        }

        public static MockPreparedStatement metaExtractForCount() throws SQLException {
            MockPreparedStatement mockPreparedStatement = mock(MockPreparedStatement.class);
            MockResultSet resultSetMock = mock(MockResultSet.class);

            MockResultSetMetaData mockResultSetMetaData = new MockResultSetMetaData();
            mockResultSetMetaData.setColumnCount(1);
            mockResultSetMetaData.setColumnLabel(1, "tmp_count");
            mockResultSetMetaData.setColumnName(1, "tmp_count");
            mockResultSetMetaData.setColumnType(1, Types.INTEGER);
            mockResultSetMetaData.setColumnClassName(1, Long.class.getName());
            given(resultSetMock.getMetaData()).willReturn(mockResultSetMetaData);
            given(resultSetMock.getLong("tmp_count")).willReturn(10L);
            given(resultSetMock.next()).willReturn(true, false);
            given(mockPreparedStatement.getResultSet()).willReturn(resultSetMock);
            return mockPreparedStatement;
        }

        @Bean
        public static DataSource dataSource() throws SQLException {

            MockConnection mockcon = mock(MockConnection.class);
            MockPreparedStatement mockPreparedStatementForPage = metaExtractForPage();
            MockPreparedStatement mockPreparedStatementForPage2 = metaExtractForPage();
            MockPreparedStatement mockPreparedStatement = metaExtractForList();
            MockPreparedStatement mockPreparedStatement2 = metaExtractForList();

            MockPreparedStatement mockPreparedStatementForVoid = metaExtractForVoid();
            MockPreparedStatement metaExtractForMap = metaExtractForMap();
            MockPreparedStatement metaExtractForCount = metaExtractForCount();
            MockPreparedStatement metaExtractForCount2 = metaExtractForCount();
            /**
             *  protected abstract Statement instantiateStatement(Connection connection) throws SQLException;
             *  connection.createStatement() => mockPrepareStatement.
             */
            given(mockcon.prepareStatement("SELECT  \n" +
                    "id,name\n" +
                    "  FROM user"))
                    .willReturn(mockPreparedStatement2);

            given(mockcon.prepareStatement("SELECT id,name,phone,version FROM user WHERE id IN ( ? , ? , ? , ? , ? , ? )"))
                    .willReturn(mockPreparedStatement2);
            given(mockcon.prepareStatement("select * from users where id=? and id1=? and orgCode=role2"))
                    .willReturn(mockPreparedStatement2);
            given(mockcon.prepareStatement("SELECT  \n" +
                    "name\n" +
                    "  FROM user \n" +
                    " \n" +
                    " WHERE (phone = ?)"))
                    .willReturn(mockPreparedStatement2);

            given(mockcon.prepareStatement("select count(0) from ( \n" +
                    "SELECT  \n" +
                    "id,name\n" +
                    "  FROM user\n" +
                    " ) tmp_count"))
                    .willReturn(metaExtractForCount);

            given(mockcon.prepareStatement("select count(0) from ( \n" +
                    "select * from users where orgCode=role1\n" +
                    " ) tmp_count", 1003, 1007))
                    .willReturn(metaExtractForCount);
            given(mockcon.prepareStatement("select count(0) from ( \n" +
                    "select * from users where orgCode=role2\n" +
                    " ) tmp_count", 1003, 1007))
                    .willReturn(metaExtractForCount2);
            given(mockcon.prepareStatement("select count(0) from ( \n" +
                    "select * from users where id=? and orgCode=role2\n" +
                    " ) tmp_count", 1003, 1007))
                    .willReturn(metaExtractForCount2);
            given(mockcon.prepareStatement("select count(0) from ( \n" +
                    "select * from users where id=? and id1=? and orgCode=role2\n" +
                    " ) tmp_count", 1003, 1007))
                    .willReturn(metaExtractForCount2);
            given(mockcon.prepareStatement("select count(0) from ( \n" +
                    "select * from users where id=? and id1=? and orgCode=role2\n" +
                    " ) tmp_count"))
                    .willReturn(metaExtractForCount2);
            given(mockcon.prepareStatement("select count(0) from ( \n" +
                    "select * from users where           \n" +
                    "            id=? and id1=? and orgCode=role2\n" +
                    " ) tmp_count", 1003, 1007))
                    .willReturn(metaExtractForCount2);

            given(mockcon.prepareStatement("select key,val from users", 1003, 1007))
                    .willReturn(metaExtractForMap);
            given(mockcon.prepareStatement("select * from users where  count=222", 1003, 1007))
                    .willReturn(mockPreparedStatement);
            given(mockcon.prepareStatement("select * from users where  count=222", 1003, 1007))
                    .willReturn(mockPreparedStatement);
            given(mockcon.prepareStatement("select * from users where  count=222 and orgCode=role1", 1003, 1007))
                    .willReturn(mockPreparedStatement);
            given(mockcon.prepareStatement("select * from users where orgCode=role1", 1003, 1007))
                    .willReturn(mockPreparedStatement);
            given(mockcon.prepareStatement("select * from users", 1003, 1007))
                    .willReturn(mockPreparedStatement);
            given(mockcon.prepareStatement("select * from users where           \n" +
                    "            id=? and id1=? and orgCode=role2", 1003, 1007))
                    .willReturn(mockPreparedStatement);
            given(mockcon.prepareStatement("select * from users where id=? and id1=? and orgCode=role2", 1003, 1007))
                    .willReturn(mockPreparedStatement);
            given(mockcon.prepareStatement("select * from users where id=? and id1=? and orgCode=role1", 1003, 1007))
                    .willReturn(mockPreparedStatement);
            given(mockcon.prepareStatement("select key,val from users where orgCode=role1", 1003, 1007))
                    .willReturn(mockPreparedStatement2);

            /**
             *  注意sql需要全部匹配
             */
            given(mockcon.prepareStatement("select * from users where orgCode=role1\n" +
                    " LIMIT ? ", 1003, 1007))
                    .willReturn(mockPreparedStatementForPage);
            given(mockcon.prepareStatement("select * from users where orgCode=role2\n" +
                    " LIMIT ? ", 1003, 1007))
                    .willReturn(mockPreparedStatementForPage2);
            given(mockcon.prepareStatement("select * from users where id=? and orgCode=role2\n" +
                    " LIMIT ? ", 1003, 1007))
                    .willReturn(mockPreparedStatementForPage2);
            given(mockcon.prepareStatement("select * from users where id=? and id1=? and orgCode=role2\n" +
                    " LIMIT ? ", 1003, 1007))
                    .willReturn(mockPreparedStatementForPage2);
            given(mockcon.prepareStatement("select * from users where           \n" +
                    "            id=? and id1=? and orgCode=role2\n" +
                    " LIMIT ? ", 1003, 1007))
                    .willReturn(mockPreparedStatementForPage2);
            given(mockcon.prepareStatement("SELECT  \n" +
                    "id,name\n" +
                    "  FROM user\n" +
                    " LIMIT ? "))
                    .willReturn(mockPreparedStatementForPage2);

            given(mockcon.prepareStatement("select ${val}", 1003, 1007))
                    .willReturn(mockPreparedStatementForVoid);
            given(mockcon.prepareStatement("select ?", 1003, 1007))
                    .willReturn(mockPreparedStatementForVoid);
            given(mockcon.prepareStatement("select 2", 1003, 1007))
                    .willReturn(mockPreparedStatementForVoid);
            given(mockcon.prepareStatement("select 3", 1003, 1007))
                    .willReturn(mockPreparedStatementForVoid);
            given(mockcon.prepareStatement("UPDATE user  SET name=?,\n" +
                    "phone=?,\n" +
                    "version=?,\n" +
                    "\n" +
                    "\n" +
                    "name=?  \n" +
                    " \n" +
                    " WHERE (phone = ? AND version = ?)"))
                    .willReturn(mockPreparedStatement);

            given(mockcon.prepareStatement("update user set name='hcj'",1003,1007)).willReturn(mockPreparedStatement);
            given(mockcon.prepareStatement("update user set name='hcj' where id=1",1003,1007)).willReturn(mockPreparedStatement2);
            given(mockcon.prepareStatement("UPDATE user  SET name=?,\n" +
                    "phone=?,\n" +
                    "version=?  \n" +
                    " \n" +
                    " WHERE (name = ? AND version = ?)")).willReturn(mockPreparedStatement2);
            given(mockcon.prepareStatement("UPDATE user  SET name=?,\n" +
                    "phone=?,\n" +
                    "version=?  \n" +
                    " \n" +
                    " WHERE (name = ? AND version = ?)")).willReturn(mockPreparedStatement);
            given(mockcon.prepareStatement("INSERT INTO user  ( id,\n" +
                    "name,\n" +
                    "phone,\n" +
                    "version )  VALUES  ( ?,\n" +
                    "?,\n" +
                    "?,\n" +
                    "? )"))
                .willReturn(mockPreparedStatement);
            given(mockcon.prepareStatement("insert into users(username) \n" +
                    "         values(  \n" +
                    "            ?\n" +
                    "         , \n" +
                    "            ?\n" +
                    "         , \n" +
                    "            ?\n" +
                    "         , \n" +
                    "            ?\n" +
                    "         )", 1003, 1007))
                    .willReturn(mockPreparedStatement);

            given(mockcon.prepareStatement("insert into users(username) \n" +
                    "         values(  \n" +
                    "            ?\n" +
                    "         , \n" +
                    "            ?\n" +
                    "         , \n" +
                    "            ?\n" +
                    "         )", 1003, 1007))
                    .willReturn(mockPreparedStatement);

            given(mockPreparedStatement.getUpdateCount()).willReturn(-1);
            given(mockPreparedStatement2.getUpdateCount()).willReturn(-1);
            given(mockPreparedStatementForVoid.getUpdateCount()).willReturn(-1);
            given(metaExtractForMap.getUpdateCount()).willReturn(-1);
            given(mockPreparedStatementForPage.getUpdateCount()).willReturn(-1);

            MockDataSource mockDataSource = mock(MockDataSource.class);
            given(mockDataSource.getConnection()).willReturn(mockcon);
            MockDatabaseMetaData mockDatabaseMetaData = new MockDatabaseMetaData();
            mockDatabaseMetaData.setURL("jdbc:mysql://localhost:3306/test");

            given(mockDataSource.getConnection().getMetaData()).willReturn(mockDatabaseMetaData);
            Assert.notNull(mockDataSource.getConnection());

            return mockDataSource;
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            if(testTypes.get()== PLUS){
//                new Delete
                OptimisticLockerInterceptor optimisticLockerInterceptor = new OptimisticLockerInterceptor();
                beanFactory.registerSingleton("optimisticLockerInterceptor",optimisticLockerInterceptor);
//                beanFactory.registerSingleton();
                BeanDefinition pgDruidDataSource = beanFactory.getBeanDefinition("pgDruidDataSource");
                pgDruidDataSource.setPrimary(false);

                BeanDefinition dataSource = beanFactory.getBeanDefinition("dataSource");
                dataSource.setPrimary(true);
            }

        }

    }

    public static AnnotationConfigApplicationContext applicationContext;
    public static AtomicReference<TestTypes> testTypes =  null;


    @BeforeEach
    public void before(TestInfo testInfo) throws SQLException {
        applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(MapperConfiguration.class);
        testTypes =new AtomicReference<>();

        testInfo.getTestClass().ifPresent(cls -> {
            if (MybatisForPG.class.isAssignableFrom(cls)) {
                testTypes.set(PG);
                // import transaction bean
                applicationContext.register(TransactionAutoConfigurationDemo.class);
            }else if(MybatisPlusTest.class.isAssignableFrom(cls)){
                testTypes.set(PLUS);
            }else{
                testTypes.set(EMBED);
            }
        });
        testTypes.get().doAction(applicationContext);

    }

    protected void startContext() {

        applicationContext.refresh();
        applicationContext.start();
        // this will throw an exception if the beans cannot be found
        applicationContext.getBean("sqlSessionFactory");
    }


    private static void setupSqlSessionFactoryForPG(AnnotationConfigApplicationContext applicationContext) throws SQLException {

        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(SqlSessionFactoryBean.class);
        definition.getPropertyValues().add("dataSource", MapperConfiguration.pgDruidDataSource());
        org.springframework.core.io.ClassPathResource classPathResource = new org.springframework.core.io.ClassPathResource("sqlmap/MapperInterface.xml");
        definition.getPropertyValues().add("mapperLocations", classPathResource);
        applicationContext.registerBeanDefinition("sqlSessionFactory", definition);

    }

    private static void setupSqlSessionFactory(AnnotationConfigApplicationContext applicationContext) throws SQLException {

        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(SqlSessionFactoryBean.class);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setDefaultResultSetType(ResultSetType.FORWARD_ONLY);
        definition.getPropertyValues().add("configuration",configuration);
        definition.getPropertyValues().add("dataSource", MapperConfiguration.dataSource());
        Interceptor interceptor = MapperConfiguration.AddOrgCodeForPageInteceptor();
        Interceptor pageInterceptor = MapperConfiguration.PageInterceptor();
        Interceptor[] interceptors = {pageInterceptor, interceptor};
        org.springframework.core.io.ClassPathResource classPathResource = new org.springframework.core.io.ClassPathResource("sqlmap/MapperInterface.xml");
        definition.getPropertyValues().add("plugins", interceptors);
        definition.getPropertyValues().add("mapperLocations", classPathResource);
        applicationContext.registerBeanDefinition("sqlSessionFactory", definition);
    }


}
