package BeanUtilsTest;

public class StringTest2 {
    public String aaa = "hello";
    public static String cccc = "dsa";
    // final修饰的常量 和 字符串常量
    public static String dddd = "";
    public final static int eeee = 2; // 默认使用包装类型 代替int  #18 = Integer            2


    private void hello() {
        String bbb = "world";
    }

    public static void main(String[] args) {
        // jdk1.8 后 字符串常量池还在堆, 运行时常量池还在方法区 ，
        // 在构造过程中不会生成“abcdef"字符串常量，原则： 未声明的，只放结果；已经声明的，只放声明
//        String s = new String("abc") + new String("def");
//        System.out.println(s);
//        String s1 = new StringBuilder(1024).append("hello").append("world").toString();
//        System.out.println(s1);
//        String s1 = "he" + "world"; // 优化成 StringBuilder , ldc 将常量池中的变量推到栈顶，
//        System.out.println(s1);
        // 0.成员变量，会在加载初始化阶段，使用到了字符串常量池,而本地变量则是在使用时，才使用到字符串常量池
        // 1.jdk1.8 中 String.intern() 可以在运行时，期间把字符串放到字符串常量池中

        String sss = new String("111");// 堆,本地常量池(也会建立一份) 因为指向不同，所以不同，
        String sss2 = "111"; // 使用本地变量的引用
        String sss3 = "111"; //
        System.out.println(sss==sss2); //
        System.out.println(sss3==sss2); //都使用本地变量的引用，所以只有一份。
        String sss4 = new String("111");
        System.out.println(sss==sss4); // 只创建一个对象，而添加多个引用而已

    }
}

