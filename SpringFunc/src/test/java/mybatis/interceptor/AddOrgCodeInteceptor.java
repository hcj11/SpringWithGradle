package mybatis.interceptor;

import lombok.Data;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;
import java.util.regex.Pattern;

@Intercepts(value = { @Signature(type = Executor.class, method = "update",
        args = {MappedStatement.class, Object.class})})
@Data
public class AddOrgCodeInteceptor implements Interceptor {
    Object target;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // add orgCode
        MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
        Object paramter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(paramter);

        String sql = boundSql.getSql();
        StringBuffer stringBuffer = new StringBuffer(1024);
        // insert into users(username) values(?,?,?);
        boundSql.getAdditionalParameter("");
        boundSql.setAdditionalParameter("","");

        return null;
    }

    @Override
    public Object plugin(Object target) {
        this.target = target;
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        properties.put("hello","world");
    }
}