import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;
@SuperBuilder
@Data
class SuperObjectTest{
    private String code;
}

@SuperBuilder
@Data
public class Demo1Test extends SuperObjectTest {

    @Data
    static class A {
        private static int count =0 ;
    }

    @BeforeEach
    public void setUp(){
        StringBuffer stringBuffer = new StringBuffer(1024);
    }
    @Test
    public void encode() {
        SuperObjectTest build = SuperObjectTest.builder().code("").build();
        Demo1Test.builder().code("").build();


        System.out.println(A.count);

        byte[] decodevals = Base64.decode("MTdhZDUyMTgtNzE4Mi00YjljLWI1YTUtYjVhYWVhYzAwYTI3");
        System.out.println(new String(decodevals));
    }

}
