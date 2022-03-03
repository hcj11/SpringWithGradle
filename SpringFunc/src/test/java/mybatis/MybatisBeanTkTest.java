package mybatis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import sample.mybatis.domain.UserTemp;
import sample.mybatis.mapper.MapperInterface;
import sample.mybatis.mapper.UserTempInterface;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

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
