package mybatis;
/**
 * spring-mybatis-boot  和 springboot  存在冲突
 */

import cn.hutool.core.lang.Assert;
import com.alibaba.druid.pool.DruidDataSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockDataSource;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import context.CompontScan;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import mybatis.interceptor.AddOrgCodeInteceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sample.mybatis.dto.UserDto;
import sample.mybatis.mapper.MapperInterface;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@ImportAutoConfiguration(MybatisAutoConfiguration.class)
class MybatisAutoConfigurationDemo {


}


@Slf4j
public class Mybatis {

    @Data
    @Configuration(proxyBeanMethods = false)
    @MapperScan(basePackages = {"sample.mybatis.mapper", "sample.mybatis.domain"})
    public static class MapperConfiguration {

        @Qualifier("mapperInterface")
        @Autowired
        private MapperInterface mapperInterface1;

        @Resource
        private MapperInterface mapperInterface2;

        @Bean
        public Interceptor AddOrgCodeInteceptor() {
            return new AddOrgCodeInteceptor();
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

        @Bean
        public static DataSource dataSource() throws SQLException {

            MockConnection mockcon = mock(MockConnection.class);
            MockPreparedStatement mockPreparedStatement = mock(MockPreparedStatement.class);
//            mockSQL  必须和BoundSql保持一致
            given(mockcon.prepareStatement("select * from users where  count=?", 1003, 1007))
                    .willReturn(mockPreparedStatement);
            given(mockcon.prepareStatement("select ${val}", 1003, 1007))
                    .willReturn(mockPreparedStatement);
            given(mockcon.prepareStatement("select ?", 1003, 1007))
                    .willReturn(mockPreparedStatement);
            given(mockcon.prepareStatement("select 2", 1003, 1007))
                    .willReturn(mockPreparedStatement);
            given(mockcon.prepareStatement("select 3", 1003, 1007))
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

            MockDataSource mockDataSource = mock(MockDataSource.class);
            given(mockDataSource.getConnection()).willReturn(mockcon);
            Assert.notNull(mockDataSource.getConnection());
            return mockDataSource;
        }
    }

    AnnotationConfigApplicationContext applicationContext;

    public void inteceptorTest() {

    }
    @Test
    public void ognlTest(){
        applicationContext.register(MapperConfiguration.class);
        startContext();
        MapperInterface bean = applicationContext.getBean(MapperInterface.class);
        Assert.notNull(bean);
        bean.query(222);

    }
    @Test
    public void aopProxySessionFactory() {
        applicationContext.register(MapperConfiguration.class, AopConfig.class);
        startContext();


        MapperInterface bean = applicationContext.getBean(MapperInterface.class);
        Assert.notNull(bean);
        List<String> list = Lists.<String>newArrayList("hcj", "王", "netfix", "network", "facebook", "alibaba", "baidu");
        List<UserDto> userDtos = new ArrayList<>();
        list.stream().forEach(l -> {
            userDtos.add(new UserDto(l));
        });
        bean.insert(userDtos);

    }

    @Test
    public void getDataSource() throws SQLException {
        Connection mockcon = mock(Connection.class);
        MockDataSource mockDataSource = mock(MockDataSource.class);
        given(mockDataSource.getConnection()).willReturn(mockcon);
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockcon);
        Assert.notNull(mockDataSource.getConnection());
        assertEquals(mockDataSource.getConnection(), mockcon);
    }

    @BeforeEach
    public void before() throws SQLException {
        applicationContext = new AnnotationConfigApplicationContext();
        setupSqlSessionFactory(applicationContext);
    }

    private void startContext() {

        applicationContext.refresh();
        applicationContext.start();
        // this will throw an exception if the beans cannot be found
        applicationContext.getBean("sqlSessionFactory");
    }

    @Test
    public void ResourceVsAutowird() {
        applicationContext.register(MapperConfiguration.class);
        startContext();
        MapperConfiguration bean = applicationContext.getBean(MapperConfiguration.class);
        Assert.isTrue(bean.getMapperInterface2() == bean.getMapperInterface1());
        Assert.isFalse(AopUtils.isCglibProxy(bean));
        CompontScan.printSingleton(applicationContext);

    }

    public static void main(String[] args) throws NoSuchMethodException {
        Method getSql = SqlProvider.class.getDeclaredMethod("getSql", String.class);
        String name = getSql.toString();
        System.out.println(name);
        // getSql vs public java.lang.String mybatis.SqlProvider.getSql()'
        Method find = MapperInterface.class.getDeclaredMethod("find", String.class);
        log.info("===0.={}", find.toString());
        //
        log.info("===1.={}", Arrays.stream(MapperInterface.class.getDeclaredMethods()).count());
        log.info("===2.={}", MapperInterface.class.getEnclosingMethod());
        log.info("===3.={}", Arrays.stream(MapperInterface.class.getMethods()).count());
        log.info("===4.={}", find.getName());

    }

    @Test
    public void try6() {
        HashMap<String, Long> map = Maps.<String, Long>newHashMap();
        map.put("failed", 0L);
        map.computeIfPresent("failed",(k,v)->{
            return v + 1;
        });
        Assert.isTrue(map.get("failed").equals(1L));;
        map.computeIfPresent("failed",(k,v)->{
            return v + 1;
        });
        Assert.isTrue(map.get("failed").equals(2L));;

        try5(map);
        Assert.isTrue(map.get("failed").equals(2L));
    }

    public void try5(HashMap<String, Long> map) {
        map.put("failed",2L);
        final Long[] successCount = {0L};
        ArrayList<String> objects = Lists.newArrayList("1", "2", "3");
        objects.stream().forEach(l -> {
            try {
                successCount[0]++;
            } catch (Exception e) {
            }
        });
        log.info("{}", successCount[0]);
        Assert.isTrue(successCount[0].equals(3L));
    }


    @Test
    public void try4() {
        Long successCount = 0L;
        ArrayList<String> objects = Lists.newArrayList("1", "2", "3");
        objects.stream().forEach(l -> {
            Long successCountFinal = successCount;
            try {
                successCountFinal++;
            } catch (Exception e) {
            }
        });
        log.info("{}", successCount);
        Assert.isTrue(successCount.equals(0L));
    }

    @Test
    public void try3() {
        int i = (int) (32 / 100); // 0
        int i1 = (int) (32 % 100); // 32
        int i2 = (int) (32 / 100) + 1; // 1
        int i4 = (int) (132 / 100) + 1; // 2
        log.info("{},{},{},{}", i, i1, i2, i4);
    }

    @Test
    public void scanMapper() throws NoSuchMethodException {

        applicationContext.register(MapperConfiguration.class);
        startContext();
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
        // 默认包名 + class名 作为bean名--configuration类
        Object bean = applicationContext.getBean("mybatis.MapperConfiguration");
        Assert.notNull(bean);
        Object mapperInterface = applicationContext.getBean("mapperInterface");
        Assert.notNull(mapperInterface);
        /**
         * mapperInterface => jdk  继承接口执行mapper的sqlsession方法
         */
        CompontScan.printSingleton(applicationContext);
        sqlSessionFactory.getConfiguration().getVariables().put("val", "3");
        String find = MapperInterface.class.getDeclaredMethod("find", String.class).getName();
        Assert.isTrue(find.equals("find"));
        try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.REUSE)) {
            /**
             * # sqlProvider
             */
            session.getMapper(MapperInterface.class).find("1");
            /**
             * $ properties
             */
            session.getMapper(MapperInterface.class).findProperties("2");
        }

    }

    @Test
    public void scanAutoConfig() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MockDataSource.class);
        context.register(MybatisAutoConfigurationDemo.class);
        context.refresh();

        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) context.getBean("sqlSessionFactory");
        CompontScan.printSingleton(context);


    }


    private void setupSqlSessionFactory(AnnotationConfigApplicationContext applicationContext) throws SQLException {
        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(SqlSessionFactoryBean.class);
        definition.getPropertyValues().add("dataSource", MapperConfiguration.dataSource());
        applicationContext.registerBeanDefinition("sqlSessionFactory", definition);
    }


}
