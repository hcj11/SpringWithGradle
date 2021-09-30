package BeanUtilsTest;

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


    public static void main(String[] args) {
//        System.out.println(X.Y.Z);
    }
}
