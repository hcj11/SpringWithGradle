package mybatis.interceptor;

import com.github.pagehelper.Page;
import com.mockrunner.util.common.MethodUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.junit.Assert;
import sample.mybatis.mapper.MapperInterface;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

import static com.github.pagehelper.BoundSqlInterceptor.Type.ORIGINAL;
import static com.github.pagehelper.page.PageMethod.getLocalPage;

@Intercepts(value = {
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
}
)
@Slf4j
@Data
public class AddOrgCodeForPageInteceptor implements Interceptor {
    private Object target;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];
        Executor executor = (Executor) invocation.getTarget();
        CacheKey cacheKey;
        BoundSql boundSql;
        //由于逻辑关系，只会进入一次
        if (args.length == 4) {
            //4 个参数时
            boundSql = ms.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
        } else {
            //6 个参数时
            cacheKey = (CacheKey) args[4];
            boundSql = (BoundSql) args[5];
        }

        Configuration configuration = ms.getConfiguration();
        String id = ms.getId();


        int i = id.lastIndexOf(".");
        String substring = id.substring(0, i);
        Class aClass = Class.forName(substring);
        Method accessibleMethod = null;
        /**
         * 默认mapper无重写function。
         * 目前无法查询到父类接口下的方法, mybatis-plus 找不到，可以尝试其他方式，
         */
        Method[] matchMethod = MethodUtil.getMatchingDeclaredMethods(aClass, id.substring(i + 1));
        if(matchMethod.length>0){
            accessibleMethod = matchMethod[0];
        }else{
            // don't condition append.
            return invocation.proceed();
        }

        Page page = getLocalPage();

        if (page != null) {

            page.setBoundSqlInterceptor(new AddClauseForBoundSqlInteceptor(configuration, accessibleMethod));

        } else {
            AddClauseForBoundSqlInteceptor addClauseForBoundSqlInteceptor = new AddClauseForBoundSqlInteceptor(configuration, accessibleMethod);

            BoundSql boundSql1 = addClauseForBoundSqlInteceptor.boundSql(ORIGINAL, boundSql, cacheKey);

            List resultList = executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql1);
            return resultList;

        }

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
        //        LinkedHashSet<Class> set = new LinkedHashSet<>();
//        if (parameter == null) {
//        } else {
//            if (parameter.getClass().isAssignableFrom(DefaultSqlSession.StrictMap.class)) {
//                DefaultSqlSession.StrictMap<Object> strictMap = (DefaultSqlSession.StrictMap<Object>) parameter;
//                if (strictMap.containsKey("list")) {
//                    set.add(List.class);
//                } else if (strictMap.containsKey("array")) {
//                    set.add(Array.class);
//                } else if (strictMap.containsKey("collection")) {
//                    set.add(Collection.class);
//                } else { // others object.
//                    set.add(ms.getParameterMap().getType());
//                }
//            } else if (parameter.getClass().isAssignableFrom(MapperMethod.ParamMap.class)) {
//                MapperMethod.ParamMap<Object> parameter1 = (MapperMethod.ParamMap<Object>) parameter;
//                parameter1.entrySet().stream().forEach((Map.Entry<String, Object> kv) -> {
//// 获取原型类型， list , array, collection , map , set,
//                            if (List.class.isAssignableFrom(kv.getValue().getClass())) {
//                                set.add(List.class);
//                            } else if (Set.class.isAssignableFrom(kv.getValue().getClass())) {
//                                set.add(Set.class);
//                            } else if (Map.class.isAssignableFrom(kv.getValue().getClass())) {
//                                set.add(Map.class);
//                            } else if (Array.class.isAssignableFrom(kv.getValue().getClass())) {
//                                set.add(Array.class);
//                            } else if (Collection.class.isAssignableFrom(kv.getValue().getClass())) {
//                                set.add(Collection.class);
//                            } else {
//                                set.add(kv.getValue().getClass());
//                            }
//
//                        }
//                );
//            } else {
//                // other object ie: int , Integer , Object.
//                // 确认是否要进行拆包或者合包。
//                // 根据type和paramter类型进行区分
//                // 如何去讲int 字段改为
//                // parameter.getClass().isAssignableFrom(MapperMethod.ParamMap.class)
//                // 若是8大基本类型的，可以考虑
//                Class<?> type = ms.getParameterMap().getType();
//                if(type==null){
//                    // int.class -> Integer.class 如何改变。
//                    Class<?> aClass = parameter.getClass();
//                    aClass.getComponentType();
//                    set.add(aClass);
//                }else{// 尝试取param1参数，而不是简单的去重。
//                    set.add(type);
//                }
//
//            }
//        }


//        Class[] classes = set.toArray(new Class[0]);
    }
}