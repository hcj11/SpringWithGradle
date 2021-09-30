package aop;

import aop.annotation.OrgCode;
import cn.hutool.core.lang.Assert;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@OrgCode
@Data
class OrgCodeDomain{
    String name;
}
@Component
public class UserService implements Shopping{
    public void showMap(Map map){}
    public void shop(String name) {
        System.out.println(name+"购票!!!");
    }
    public void shopError(String name) {
       throw new RuntimeException("error");
    }
    public void addCols(OrgCodeDomain name){
        Assert.isTrue(name.getName().equals("威海市--weihai"));
    }
    public void addColsWithMap(OrgCodeDomain name, HashMap hashMap) {
        Assert.isTrue(name.getName().equals("威海市--weihai"));
    }
    public void addColsWithString(String name) {
        Assert.isFalse(name.equals("威海市--weihai"));
    }


}
