package mybatis;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.mockrunner.mock.jdbc.MockDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.mockito.Mockito;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import sample.mybatis.domain.User;
import sample.mybatis.dto.UserDto;
import sample.mybatis.mapper.MapperInterface;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.pagehelper.page.PageMethod.startPage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@EnabledIf("true")
@Slf4j
public class PluginUnionTest extends Mybatis {

    @Test
    public void aopProxySessionFactory() {
        applicationContext.register(MapperConfiguration.class, AopConfig.class);
        startContext();
        MapperInterface bean = applicationContext.getBean(MapperInterface.class);
        Assert.notNull(bean);
        List<String> list = Lists.<String>newArrayList("hcj", "baidu", "netfix", "network", "facebook", "alibaba", "baidu");
        List<UserDto> userDtos = new ArrayList<>();
        list.stream().forEach(l -> {
            userDtos.add(new UserDto(l));
        });
        bean.insertList(userDtos);
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

    @Test
    public void ognlTestForListMap() {
        applicationContext.register(MapperConfiguration.class);
        startContext();
        MapperInterface bean = applicationContext.getBean(MapperInterface.class);
        Assert.notNull(bean);
        ArrayList<String> strings = Lists.newArrayList("hello world", "hcj1", "hcj2", "hcj3");
        List<Map<String, String>> query2 = bean.query2();
        log.info("query2:{}", query2);
        query2.stream().forEach(map -> {
            Assert.isTrue(strings.contains(map.get("list")));

        });


    }
    @Test
    public void ognlTestForPageWithList() throws Exception {
        applicationContext.register(MapperConfiguration.class);
        startContext();
        MapperInterface bean = applicationContext.getBean(MapperInterface.class);
        Assert.notNull(bean);
        Page<String> objects2 = startPage(1, 10);
        List<String> list = new ArrayList<String>() {
            {
                add("val");
            }
        };
        Page<String> strings2 = bean.queryWithPageAndRole2AndList(list);
        log.info("ognlTestForPageWithList page resutlt2:{}", strings2);
        assertEquals(strings2.get(0), "hello world");
    }

    @Test
    public void ognlTestForPageWithMap() throws Exception {
        applicationContext.register(MapperConfiguration.class);
        startContext();
        MapperInterface bean = applicationContext.getBean(MapperInterface.class);
        Assert.notNull(bean);
        Page<String> objects2 = startPage(1, 10);
        HashMap<String, Object> hashMap = new HashMap<String, Object>() {
            {
                put("val", "222");
                put("val2", 231);
            }
        };
        Page<String> strings2 = bean.queryWithPageAndRole2AndMap(hashMap);
        log.info("ognlTestForPageWithTwoParam page resutlt2:{}", strings2);
        assertEquals(strings2.get(0), "hello world");
    }

    @Test
    public void ognlTestForPageWithTwoParam() throws Exception {
        applicationContext.register(MapperConfiguration.class);
        startContext();
        MapperInterface bean = applicationContext.getBean(MapperInterface.class);
        Assert.notNull(bean);
// 多参数 构建成 。 ParamMap ，具体看源码。
        Page<String> objects2 = startPage(1, 10);
        Page<String> strings2 = bean.queryNoClauseWithPageAndRole2AndTwoParam("222", 231);
        log.info("ognlTestForPageWithTwoParam page resutlt2:{}", strings2);
        assertEquals(strings2.get(0), "hello world");
    }

    @Test
    public void ognlTestForPageWithParam() throws Exception {
        applicationContext.register(MapperConfiguration.class);
        startContext();
        MapperInterface bean = applicationContext.getBean(MapperInterface.class);
        Assert.notNull(bean);

        Page<String> objects2 = startPage(1, 10);
        Page<String> strings2 = bean.queryNoClauseWithPageAndRole2AndParam("222");
        log.info("ognlTestForPageWithParam page resutlt2:{}", strings2);
        assertEquals(strings2.get(0), "hello world");
    }

    @Test
    public void ognlTestForPage() throws Exception {
        applicationContext.register(MapperConfiguration.class);
        startContext();
        MapperInterface bean = applicationContext.getBean(MapperInterface.class);
        Assert.notNull(bean);


        Page<String> objects2 = startPage(1, 10);
        Page<String> strings2 = bean.queryNoClauseWithPageAndRole2();
        List<String> result2 = strings2.getResult();
        log.info("queryNoClauseWithPage2 page toString2:{}, resutlt2:{}", strings2, result2);
        assertEquals(result2.get(0), "hello world");

        Page<String> objects1 = startPage(1, 10);
        Page<String> strings1 = bean.queryNoClauseWithPageAndRole1();
        List<String> result1 = strings1.getResult();
        log.info("queryNoClauseWithPage1 page toString1:{}, resutlt1:{}", strings1, result1);
        assertEquals(result1.get(0), "hello world");

        List<String> strings = bean.queryNoClause();
        log.info("queryNoClause list result:{}", strings);
        assertEquals(strings.get(0), "hello world");

    }
    /**
     mockito  proxy the
     public PreparedStatement prepareStatement(String sql) throws SQLException 而不是
     public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
     */
    @Test
    public void ognlTestForListWithoutParamAnnotationInXML() {
        applicationContext.register(MapperConfiguration.class);
        startContext();
        MapperInterface bean = applicationContext.getBean(MapperInterface.class);
        Assert.notNull(bean);
        List<String> list = bean.queryWithParamAnnotationInXML(222);
        List<String> list2 = bean.queryWithoutParamAnnotationInXML(222, 223);
        log.info("queryWithoutParamAnnotationInXML list result:{}", list);
    }

    @Test
    public void ognlTestForListWithoutParamAnnotation() {
        applicationContext.register(MapperConfiguration.class);
        startContext();
        MapperInterface bean = applicationContext.getBean(MapperInterface.class);
        Assert.notNull(bean);
//        assertThrows(MyBatisSystemException.class, () -> {
            bean.queryWithoutParamAnnotation(222);
//        });
        bean.queryWithPrimitive(1, 2);

        List<String> list = new ArrayList<String>() {
            {
                add("val");
            }
        };
        List<String> strings2 = bean.queryWithPageAndRole2AndListWithoutParamAnnotation(list);
        log.info("queryWithPageAndRole2AndListWithoutParamAnnotation list result:{}", strings2);
        HashMap<String, Object> hashMap = new HashMap<String, Object>() {
            {
                put("val", "222");
                put("val2", 231);
            }
        };
        List<String> strings3 = bean.queryWithRole2AndMapWithoutParamAnnotation(hashMap);
        log.info("queryWithRole2AndMapWithoutParamAnnotation list result:{}", strings3);
        UserDto userDto = new UserDto("王小明");

        List<String> strings4 = bean.queryWithRole2AndObjectWithoutParamAnnotation(userDto);
        log.info("queryWithRole2AndObjectWithoutParamAnnotation list result:{}", strings4);
    }

    @Test
    public void ognlTestForList() {
        applicationContext.register(MapperConfiguration.class);
        startContext();
        MapperInterface bean = applicationContext.getBean(MapperInterface.class);
        Assert.notNull(bean);
        List<String> query = bean.query(222);
        log.info("query list resutlt:{}", query);
        List<String> strings = bean.queryNoClause();
        log.info("queryNoClause list result:{}", strings);
        List<String> list = new ArrayList<String>() {
            {
                add("val");
            }
        };
        List<String> strings2 = bean.queryWithPageAndRole2AndList(list);
        log.info("queryWithPageAndRole2AndList list result:{}", strings2);
    }

    @Test
    public void ognlTestForMap() {
        applicationContext.register(MapperConfiguration.class);
        startContext();
        MapperInterface bean = applicationContext.getBean(MapperInterface.class);
        Assert.notNull(bean);
        ArrayList<String> strings = Lists.newArrayList("hcj", "hcj1", "hcj2", "hcj3");
        Map<String, String> query1 = bean.query1();
        query1.get("val");

        log.info("query1:{}", query1);
    }
}
