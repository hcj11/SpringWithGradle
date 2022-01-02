import org.junit.jupiter.api.Test;

public class CaseTest {
    public static class X {
        // new ,class 变量进行初始化 set,get ，若未初始化的话
        private String name = "black";
        static Z y = new Z();
    }

    public static class Y extends X {

    }

    public static class Z extends X{
        private String name = "red";
    }
    @Test
    public void test1(){

    }


}
