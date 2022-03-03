package mybatis.interceptor;

import com.github.pagehelper.Page;
import com.mockrunner.util.common.MethodUtil;
import context.CompontScan;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import mybatis.CustomRoutingDataSource;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import sample.mybatis.annotation.EnableMysql;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.github.pagehelper.BoundSqlInterceptor.Type.ORIGINAL;
import static com.github.pagehelper.page.PageMethod.getLocalPage;
@Intercepts(value = {
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update",
                args = {MappedStatement.class, Object.class}),
}
)
@Slf4j
@Data
public class DataSourceRouteInteceptor implements Interceptor {
    private Object target;
    private DataSource dataSource;

    public DataSourceRouteInteceptor(CustomRoutingDataSource customRoutingDataSource) {
        this.dataSource=customRoutingDataSource;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
//        CustomRoutingDataSource dataSource = (CustomRoutingDataSource) this.dataSource;
//        Object[] args = invocation.getArgs();
//        MappedStatement ms = (MappedStatement) args[0];
//
//        Configuration configuration = ms.getConfiguration();
//        String id = ms.getId();
//
//
//        int i = id.lastIndexOf(".");
//        String substring = id.substring(0, i);
//        Class aClass = Class.forName(substring);
//        Method accessibleMethod = null;
//        /**
//         * 默认mapper无重写function。
//         * 目前无法查询到父类接口下的方法, mybatis-plus 找不到，可以尝试其他方式，
//         */
//        Method[] matchMethod = MethodUtil.getMatchingDeclaredMethods(aClass, id.substring(i + 1));
//        if(matchMethod.length>0){
//            accessibleMethod = matchMethod[0];
//        }else{
//            // don't condition append.
//            return invocation.proceed();
//        }
//        if(accessibleMethod.isAnnotationPresent(EnableMysql.class)){
//            log.info("====load the mysqldatasource,");
//            /**
//             * change the connection underneath datasource.
//             * 改变sql。connection已经绑定，故在aop代理层面进行拦截处理吧，
//             */
//            dataSource.loadBalancePolicy(2);
//        };

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        this.target = target;
        return Plugin.wrap(target, this);
    }


    @Override
    public void setProperties(Properties properties) {

    }
    public void findMethodForClass(){
    }
}