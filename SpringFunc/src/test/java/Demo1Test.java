import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

public class Demo1Test {


    @Test
    public void encode() {

        byte[] decodevals = Base64.decode("MTdhZDUyMTgtNzE4Mi00YjljLWI1YTUtYjVhYWVhYzAwYTI3");
        System.out.println(new String(decodevals));
    }

}
