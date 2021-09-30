package tomcat;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

enum Types {

    A("A", "1"), B("B", "2");
    @Setter
    @Getter
    String key;
    @Setter
    @Getter
    String val;

    Types(String a, String s) {
        this.key = a;
        this.val = s;
    }

    public String getVal() {
        return val;
    }

}

public class RpcCallback {
    private static HashMap<Types, String> map = Maps.<Types, String>newHashMap();

    static {
        map.put(Types.A, "hello world");
        map.put(Types.B, "B");
    }

    public static void main(String[] args) {
        Set<Map.Entry<Types, String>> entries = RpcCallback.map.entrySet();
        entries.toArray();

        Types a = Types.valueOf("A");
        System.out.println(a);

    }
}
