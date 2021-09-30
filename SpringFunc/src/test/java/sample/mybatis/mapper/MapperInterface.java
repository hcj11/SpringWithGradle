package sample.mybatis.mapper;

import mybatis.SqlProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import sample.mybatis.annotation.OrgCode;
import sample.mybatis.annotation.SqlCheck;
import sample.mybatis.dto.UserDto;

import java.util.List;
import java.util.Map;


public interface MapperInterface {
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
    public void insert(@Param("userDtoList") List<UserDto> userDtoList);

    // bind name="a" value=""/> ²ÎÊý°ó¶¨ +  ognl test
    @Select(value = "<script>" +
            "<bind name=\"a\" value=\"@mybatis.parser.Parser$ItemType@A.getValue()\"/> " +
            "select * from users where <if test=\"count==111\">count=#{a,jdbcType=VARCHAR}</if><if test=\"count==222\">count=222</if>" +
            "</script>")
    public void query(@Param("count")Integer count);

    /**
     *
     */
//    @SqlCheck
//    @Insert(value = "<script>" +
//            " insert into users(username,org_code) \n" +
//            "        <foreach collection=\"userDtoList\" open=\"values(\" close=\")\" separator=\",\" item=\"userDto\">\n" +
//            "            #{userDto.username,jdbcType=VARCHAR},#{orgCode,jdbcType=VARCHAR}\n" +
//            "        </foreach>" +
//            "</script>")
//    public void insert(@Param("userDtoList") List<UserDto> userDtoList,@Param("orgCode")@OrgCode String orgCode);
}
