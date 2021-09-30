package BeanUtilsTest;

public class Test {
    static {
        i = 0; // 但是可以未该变量赋值。 但是按照先后顺序，最终赋值为1
//        System.out.println(i); 不合法的向前引用
    }
// statis 只能访问到定义在它之前的 变量
    static int i = 1;
    // final 修饰的 已经位于 常量 中，而不是在其他地方
//    static final String s=new String("111");
    // 而非final修饰的成员变量，发现也位于常量池中
//
//    StringBuilder stringBuilder = new StringBuilder(1024).append("hello world");
//    String s=new String("111");

    public static void main(String[] args) {
        System.out.println(Test.i);

    }
}
