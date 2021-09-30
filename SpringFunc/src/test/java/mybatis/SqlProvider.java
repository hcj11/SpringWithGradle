package mybatis;

import cn.hutool.core.lang.Assert;
import com.alibaba.druid.sql.parser.SQLSelectParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.type.JdbcType;
@Slf4j
public class SqlProvider {
    /**
     * replace, org.apache.ibatis.type.JdbcType.varchar
     */
    public String getSql(@Param("val") String val) {
        return "select #{val , jdbcType=VARCHAR}";
    }

    public String getSqlProperties(@Param("val") String val) {
        return "select ${val}";
    }
    public static void main(String[] args) {
//        SQLSelectParser ;

        // 48 ,49
        char ints = '0';
        int ints1 = ints;
        log.info("{}",ints1);

    }
}
