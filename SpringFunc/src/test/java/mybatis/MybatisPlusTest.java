package mybatis;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mockrunner.util.common.MethodUtil;
import context.CompontScan;
import lombok.extern.slf4j.Slf4j;
import mybatis.interceptor.AddClauseForBoundSqlInteceptor;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.yaml.snakeyaml.Yaml;
import sample.mybatis.domain.User;
import sample.mybatis.mapper.MapperInterface;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.github.pagehelper.page.PageMethod.startPage;

@Slf4j
public class MybatisPlusTest extends Mybatis{
    @Test
    public void try1(){
        applicationContext.register(MybatisPlusConfigurationDemo.class);
        startContext();
        CompontScan.printSingleton(applicationContext);
    }

    @Test
    public void propertiesTest(){
        applicationContext.register(MybatisPlusConfigurationDemo.class);
        startContext();
        StandardEnvironment bean = applicationContext.getBean(StandardEnvironment.class);
        MutablePropertySources propertySources = bean.getPropertySources();
        log.info("{}",propertySources);

        // mybatis load properties
        MybatisPlusProperties mybatisPlusProperties = applicationContext.getBean(MybatisPlusProperties.class);
        log.info("mybatisPlusProperties:{}",mybatisPlusProperties);



    }
    public MapperInterface makeUpContext(){
        applicationContext.register(MybatisPlusConfigurationDemo.class);
        startContext();
        MapperInterface mapperInterface = applicationContext.getBean(MapperInterface.class);
        return mapperInterface;
    }
    /**
     todo
     */
    @Test
    public void ymlTransformToMybatisProperties(){
        MapperInterface mapperInterface = makeUpContext();
        MybatisPlusProperties mybatisPlusProperties = applicationContext.getBean(MybatisPlusProperties.class);
        log.info("{}",mybatisPlusProperties);
    }

    @Test
    public void lambdaTestForDetele(){

    }
    @Test
    public void lambdaTestForUpdate(){
        /**
         LambdaUpdateWrapper is the subclass of Wrapper ,so optimisticInterceptor can think it as  ew.
         */
        MapperInterface mapperInterface = makeUpContext();
        Assert.notNull(mapperInterface);
        User hcj = User.builder().name("hcj").phone("173172").version(1).build();
        LambdaUpdateWrapper<User> eq = Wrappers.<User>lambdaUpdate().set(User::getName, "hcj").eq(User::getPhone,"173172");
        int update = mapperInterface.update(hcj, eq);
    }
    /**
     todo
     */
    @Test
    public void javaEscape(){
        StringEscapeUtils.unescapeJava("");
    }
    @Test
    public void lambdaTest(){
        MapperInterface mapperInterface = makeUpContext();
        Assert.notNull(mapperInterface);
        com.github.pagehelper.Page<User> objects = startPage(1, 10);
        LambdaQueryWrapper<User> select = Wrappers.<User>lambdaQuery()
                .select(User.class, (com.baomidou.mybatisplus.core.metadata.TableFieldInfo tableFieldInfo) -> {
                    return tableFieldInfo.getColumn().equals("name");
        });
        mapperInterface.selectList(select);
        log.info("page:{}",objects.toString());
    }
    @Test
    public void versionTest(){
        /**
          乐观锁，目前搭配  the lock of  thread level or database level
         或者循环查询，并更新版本，
         */
        MapperInterface mapperInterface = makeUpContext();
        Assert.notNull(mapperInterface);
        User hcj = User.builder().name("hcj").phone("173172").version(1).build();

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        UpdateWrapper<User> eq = updateWrapper.eq("name", "hcj");
        int update1 = mapperInterface.update(hcj, eq);

        UpdateWrapper<User> updateWrapper2 = new UpdateWrapper<>();
        UpdateWrapper<User> eq2 = updateWrapper2.eq("name", "hcj");
        int update2 = mapperInterface.update(hcj, eq2);

    }

    @Test
    public void IdGenerateTest(){
        // 乐观锁。
        MapperInterface mapperInterface = makeUpContext();
        Assert.notNull(mapperInterface);
        User hcj = User.builder().name("hcj").phone("173172").version(1).build();
        mapperInterface.insert(hcj);
    }

    /**
      make up the
     */
    @Test
    public void wrapperTest(){
        MapperInterface mapperInterface = makeUpContext();
        User user = mapperInterface.selectOne(new QueryWrapper<User>().select("name").eq("phone", "173172"));
        log.info("{}",user);
        List<User> users = mapperInterface.selectBatchIds(Lists.newArrayList(1, 2, 3, 4, 5, 6));

    }
}
