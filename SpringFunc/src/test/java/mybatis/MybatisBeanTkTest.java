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
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sample.mybatis.domain.UserTemp;
import sample.mybatis.mapper.MapperInterface;
import sample.mybatis.mapper.UserTempInterface;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Slf4j
public class MybatisBeanTkTest extends MybatisBean {
    @Test
    public void tkExampleTest(){
        UserTempInterface userTempInterface = (UserTempInterface)makeUpContext(UserTempInterface.class);

        Example.Builder builder = Example.builder(UserTemp.class);
        Example build = builder.andWhere(Sqls.custom().andEqualTo("remark", "hcj")).build();
        userTempInterface.selectCountByExample(build);



    }

    /**
     tk.mybatis.mapper
     */
    @Test
    public void tkMybatisTest(){
        //
        UserTemp others = UserTemp.builder().remark("others").build();
        UserTempInterface userTempInterface = (UserTempInterface)makeUpContext(UserTempInterface.class);
        userTempInterface.insertSelective(others);
    }

    public MapperInterface makeUpContext(){
        return (MapperInterface)makeUpContext(MapperInterface.class);
    }
    public Object makeUpContext(Class cls){
        applicationContext.register(CustomMapperScannerConfigurer.class);
        startContext();
        Object bean = applicationContext.getBean(cls);
        return bean;
    }

    public void insertTest(){
    }
}
