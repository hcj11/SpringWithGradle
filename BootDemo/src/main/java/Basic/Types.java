
package Basic;


import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public enum Types {
    A("a", "值a"),
    B("b", "值b");

    private final String key;
    private final String value;

    Types(String a, String b) {
        this.key = a;
        this.value = b;
    }

    public static String getValue(String a) {
        final String[] val = {null};
        Arrays.stream(Types.values()).forEach(s->{
            if(s.key.equals(a)){
                val[0] =s.value;
            }
        });
        return val[0];
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String b = new String(Types.getValue("b").getBytes("gbk"),"utf-8");
        System.out.println(b);

    }
}
