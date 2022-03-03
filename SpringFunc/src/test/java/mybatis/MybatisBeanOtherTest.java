package mybatis;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Assert;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mockrunner.mock.jdbc.MockDataSource;
import com.mockrunner.util.common.MethodUtil;
import context.CompontScan;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.helpers.Util;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sample.mybatis.mapper.MapperInterface;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
@Slf4j
public class MybatisBeanOtherTest extends MybatisBean {

    @Test
    public void cacheTest(){
        Class<Cache> cacheClass = Cache.class;

    }
    public void inteceptorTest() {
        applicationContext.register(MapperConfiguration.class);
        startContext();
        MapperInterface bean = applicationContext.getBean(MapperInterface.class);
        Assert.notNull(bean);

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
        map.computeIfPresent("failed", (k, v) -> {
            return v + 1;
        });
        Assert.isTrue(map.get("failed").equals(1L));
        ;
        map.computeIfPresent("failed", (k, v) -> {
            return v + 1;
        });
        Assert.isTrue(map.get("failed").equals(2L));
        ;

        try5(map);
        Assert.isTrue(map.get("failed").equals(2L));
    }

    public void try5(HashMap<String, Long> map) {
        map.put("failed", 2L);
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
    public void providerTest() throws NoSuchMethodException {

        applicationContext.register(MapperConfiguration.class);
        startContext();
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
        MapperInterface mapperInterface = (MapperInterface)applicationContext.getBean("mapperInterface");


    }
    @Test
    public void scanMapper() throws NoSuchMethodException {

        applicationContext.register(MapperConfiguration.class);
        startContext();
        CompontScan.print(applicationContext);
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
        Object bean = applicationContext.getBean("mybatisBean.MapperConfiguration");
        Assert.notNull(bean);
        Object mapperInterface = applicationContext.getBean("mapperInterface");
        Assert.notNull(mapperInterface);
        /**
         * mapperInterface => jdk
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
    @Test
    public void findResource() {

        org.springframework.core.io.ClassPathResource classPathResource = new org.springframework.core.io.ClassPathResource("sqlmap/MapperInterface.xml");
        String path1 = classPathResource.getPath();
        log.info("path1:{}", path1);

        ClassPathResource urlResource = new ClassPathResource("sqlmap/*.xml");
        String path = urlResource.getPath();
        log.info("path:{}", path);
        // todo 通配符匹配扫描文件。
//        ClassPathMapperScanner classPathMapperScanner =new ClassPathMapperScanner();


    }

    @Test
    public void findMethodForClass() {
        Class cls = MapperInterface.class;
        Method[] queryWithoutParamAnnotationInXMLS = MethodUtil.getMatchingDeclaredMethods(cls, "queryWithoutParamAnnotationInXML");
        org.junit.Assert.assertTrue(queryWithoutParamAnnotationInXMLS[0].getName().equals("queryWithoutParamAnnotationInXML"));
    }

}
