package mybatis;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.EnumTypeHandler;
import org.junit.Test;

import java.util.Arrays;

enum UpTypes{
    UP("up"),DOWN("down");
    public String names;
    UpTypes(String names){
        this.names=names;
    }
    public String getName() {
        return names;
    }
    public void setName(String names) {
        this.names = names;
    }

}
@Slf4j
public class TypeHandlerTest {
    @Test
    public void try0(){

    }
    /**
     *
     */
    @Test
    public void try1() {
        Class<EnumTypeHandler> enumTypeHandlerClass = EnumTypeHandler.class;
        Class<EnumOrdinalTypeHandler> enumOrdinalTypeHandlerClass = EnumOrdinalTypeHandler.class;

        /**
         * database -> java memory  תUP" => UpTypes.name();
         */
        UpTypes up = Enum.valueOf(UpTypes.class, "UP");

        Assert.isTrue(up==UpTypes.UP);
        Arrays.stream(UpTypes.values()).forEach(s->{
            log.info("==={},{}",s,s.names);
        });



    }
}
