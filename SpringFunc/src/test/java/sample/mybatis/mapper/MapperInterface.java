package sample.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import mybatis.SqlProvider;
import org.apache.ibatis.annotations.*;
import sample.mybatis.annotation.ROLE1;
import sample.mybatis.annotation.ROLE2;
import sample.mybatis.annotation.SqlCheck;
import sample.mybatis.domain.User;
import sample.mybatis.dto.UserDto;

import java.util.List;
import java.util.Map;


public interface MapperInterface extends BaseMapper<User> {

    @Update(value = "<script>update users set user_group_name='党员<if test=\"count==3\">2</if><if test=\"count!=3\">${count}</if>' where id=#{count}</script>")
    public void outerUpdate(@Param(value = "count") int count);
    @Update(value = "<script>update users set name='hcj<if test=\"count==3\">2</if><if test=\"count!=3\">${count}</if>' where id=#{count}</script>")
    public void nestUpdate(@Param(value = "count") int count);

    public void checkNull(Map params);

    @SelectProvider(method ="getSql" , type = SqlProvider.class)
    public void find(@Param("val")String val);

    @SelectProvider(method ="getSqlProperties" , type = SqlProvider.class)
//    @Select("select ${val}")
    public void findProperties(@Param("val")String val);
    /**
     * mock dataSource
     */
    @SqlCheck
    @Insert(value = "<script>" +
            " insert into users(username) \n" +
            "        <foreach collection=\"userDtoList\" open=\"values(\" close=\")\" separator=\",\" item=\"userDto\">\n" +
            "            #{userDto.username,jdbcType=VARCHAR}\n" +
            "        </foreach>" +
            "</script>")
    public void insertList(@Param("userDtoList") List<UserDto> userDtoList);

    // <bind name="a" value=""/> +  ognl test

    @Select(value = "<script>" +
            "<bind name=\"a\" value=\"@mybatis.parser.Parser$ItemType@A.getValue()\"/> " +
            "select * from users where <if test=\"count==111\">count=#{a,jdbcType=VARCHAR}</if><if test=\"count==222\">count=222</if>" +
            "</script>")
    public List<String> query(@Param("count")Integer count);
    /**
     Integer.class , 如何 确认。
     */
    @ROLE2
    public List<String> queryWithoutParamAnnotationInXML(int count,int count1);
    @ROLE2
    public List<String> queryWithParamAnnotationInXML(@Param("count") int count);

    @Select(value = "<script>" +
            "<bind name=\"a\" value=\"@mybatis.parser.Parser$ItemType@A.getValue()\"/> " +
            "select * from users where <if test=\"count==111\">count=#{a,jdbcType=VARCHAR}</if><if test=\"count==222\">count=222</if>" +
            "</script>")
    public List<String> queryWithoutParamAnnotation(int count);

    @Select(value = "<script>" +
            "select * from users" +
            "</script>")
    public List<String> queryNoClause();
    @Select(value = "<script>" +
            "select * from users where id=#{val} and id1=#{val2}" +
            "</script>")
    public List<String> queryWithPrimitive(int val,Integer val2);

    @ROLE1
    @Select(value = "<script>" +
            "select * from users" +
            "</script>")
    public Page<String> queryNoClauseWithPageAndRole1();
    @ROLE2
    @Select(value = "<script>" +
            "select * from users" +
            "</script>")
    public Page<String> queryNoClauseWithPageAndRole2();

    @ROLE2
    @Select(value = "<script>" +
            "select * from users where id=#{val}" +
            "</script>")
    public Page<String> queryNoClauseWithPageAndRole2AndParam(@Param(value = "val")String val);

    @ROLE2
    @Select(value = "<script>" +
            "select * from users where id=#{val} and id1=#{val2}" +
            "</script>")
    public Page<String> queryNoClauseWithPageAndRole2AndTwoParam(@Param(value = "val")String val,@Param(value = "val2")Integer val2);
    @ROLE2
    @Select(value = "<script>" +
            "select * from users where id=#{val} and id1=#{val2}" +
            "</script>")
    public Page<String> queryWithPageAndRole2AndMap(Map<String,Object> map);
    /**
     * foreach.
     */
    @ROLE2
    @Select(value = "<script>" +
            "select * from users where " +
            "        <foreach collection=\"list1\"  item=\"l\">\n" +
            "            id=#{l,jdbcType=VARCHAR} and id1=#{l,jdbcType=VARCHAR}\n" +
            "        </foreach>" +
            "</script>")
    public Page<String> queryWithPageAndRole2AndList(@Param(value = "list1") List list);
    @ROLE2
    @Select(value = "<script>" +
            "select * from users where " +
            "        <foreach collection=\"list\"  item=\"l\">\n" +
            "            id=#{l,jdbcType=VARCHAR} and id1=#{l,jdbcType=VARCHAR}\n" +
            "        </foreach>" +
            "</script>")
    public Page<String> queryWithPageAndRole2AndListWithoutParamAnnotation(List list);

    @ROLE2
    @Select(value = "<script>" +
            "select * from users where id=#{val} and id1=#{val2}" +
            "</script>")
    public List<String> queryWithRole2AndMapWithoutParamAnnotation(Map<String,Object> map);
    @ROLE2
    @Select(value = "<script>" +
            "select * from users where id=#{username} and id1=#{username}" +
            "</script>")
    public List<String> queryWithRole2AndObjectWithoutParamAnnotation(UserDto userDto);

    @Select(value = "select key,val from users")
    public Map<String,String> query1();

    @Select(value = "select key,val from users")
    public List<Map<String,String>> query2();

    @Select(value = "select * from test;")
    public List<Map<String,String>> queryInPg();
    /**
     todo 转义 =》  ?
     */
    @Select(value = "<script>" +
            " select jsonbs,id,jsonbs->'array',jsonbs->'subobj'->'subKey',jsonbs @> '{\"key\":\"val1\"}', " +
            " jsonbs @> '{\"grant_type\":\"admin\"}'" +
            " ,jsonbs ?? 'grant_type'" +
            " from public.test;" +
            "</script>")
    public List<Map<String,String>> queryJsonBInPg(@Param("val") int val);

}
