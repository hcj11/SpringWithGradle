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
        System.out.println(name+"sold out !!!");
    }
    public void shopError(String name) {
       throw new RuntimeException("error");
    }
    public void addCols(OrgCodeDomain name){
        Assert.isTrue(name.getName().equals("hello--weihai"));
    }
    public void addColsWithMap(OrgCodeDomain name, HashMap hashMap) {
        Assert.isTrue(name.getName().equals("hellow--weihai"));
    }
    public void addColsWithString(String name) {
        Assert.isFalse(name.equals("hello--weihai"));
    }


}
