package aop;

import aop.annotation.OrgCode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public interface Shopping {
    public void shop(String name);
    public void shopError(String name);
    public void showMap(Map map);
    public void addCols(OrgCodeDomain name);
    public void addColsWithMap(OrgCodeDomain name, HashMap hashMap);
    public void addColsWithString(String name);
}
