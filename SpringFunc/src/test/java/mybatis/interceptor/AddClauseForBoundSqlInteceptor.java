package mybatis.interceptor;

import com.github.pagehelper.BoundSqlInterceptor;
import com.github.pagehelper.PageException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import sample.mybatis.annotation.ROLE2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

/**
 若存在分页需求，可以尝试内嵌到pageInterceptor.
 *
 */
@Slf4j
@Data
public class AddClauseForBoundSqlInteceptor implements BoundSqlInterceptor {
    private Configuration configuration;
    private Method method;

    public AddClauseForBoundSqlInteceptor(Configuration configuration,Method method){
        this.configuration=configuration;
        this.method=method;
    }
    public BoundSql boundSql(Type type, BoundSql boundSql,CacheKey cacheKey) {
        return boundSql(type,boundSql,cacheKey,null);

    }
    @Override
    public BoundSql boundSql(Type type, BoundSql boundSql, CacheKey cacheKey, Chain chain) {

        if(BoundSqlInterceptor.Type.ORIGINAL!=type){
            return boundSql;
        }
        String role="role1";
        if(method.isAnnotationPresent(ROLE2.class)){
                role="role2";
        };

        String sql = boundSql.getSql();
        log.info("====log==={},{},{}",type,sql,cacheKey.toString());
        int indexStart=-1;
        StringBuffer stringBuffer = new StringBuffer(1024);
        stringBuffer.append(sql);
        if((indexStart=sql.indexOf("where"))!=-1){
            stringBuffer.append(" and orgCode=").append(role);
        }else{
            stringBuffer.append(" where orgCode=").append(role);
        }


        Object parameterObject = boundSql.getParameterObject();

        BoundSql addOrgCodeBoundSql = new BoundSql(configuration, stringBuffer.toString(),
                boundSql.getParameterMappings(), parameterObject);

        Map<String, Object> additionalParameters = getAdditionalParameter(boundSql);

        //当使用动态 SQL 时，可能会产生临时的参数，这些参数需要手动设置到新的 BoundSql 中
        for (String key : additionalParameters.keySet()) {
            addOrgCodeBoundSql.setAdditionalParameter(key, additionalParameters.get(key));
        }

        return addOrgCodeBoundSql;
    }
    private static Field additionalParametersField;

    private static Field providerMethodArgumentNamesField;

    static {
        try {
            additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
            additionalParametersField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new PageException("获取 BoundSql 属性 additionalParameters 失败: " + e, e);
        }
        try {
            //兼容低版本
            providerMethodArgumentNamesField = ProviderSqlSource.class.getDeclaredField("providerMethodArgumentNames");
            providerMethodArgumentNamesField.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }
    public static Map<String, Object> getAdditionalParameter(BoundSql boundSql) {
        try {
            return (Map<String, Object>) additionalParametersField.get(boundSql);
        } catch (IllegalAccessException e) {
            throw new PageException("获取 BoundSql 属性值 additionalParameters 失败: " + e, e);
        }
    }
}