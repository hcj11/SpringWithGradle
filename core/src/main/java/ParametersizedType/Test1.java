package ParametersizedType;

public class Test1 {
    public static void test2(){
        // 但是 Byte、Short、Integer、Long、Character 这5种整型的包装类也只是在对应值在 [-128,127] 时才会使用缓冲池，
        // 超出此范围仍然会去创建新的对象
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        System.out.println(c == d); // true
        System.out.println(e == f); // false
        System.out.println(c == (a + b)); // true
        System.out.println(c.equals(a + b)); // true
        System.out.println(g == (a + b)); // true
        System.out.println(g.equals(a + b));// false   Long vs Integer
        // 包装类的“==” 运算在不遇到算术运算的情况下不会自动拆箱。以及他们的equals方法不处理数据转型的关系， 建议避免这样查询和装箱
    }
    public static void test1(){
        // 基本类型 ，判断是否相等。
        int a = 1;
        int b = 2;
        int c = 3;
        int d = 3;
        int e = 321;
        long f = 321;
        long g = 3L;
        System.out.println(c == d); // true
        System.out.println(e == f); // true
        System.out.println(c == (a + b)); // true
        System.out.println(g == (a + b)); // true
    }
    public static void main(String[] args) {
//       test1();
       test2();

    }
}
