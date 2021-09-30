package BeanUtilsTest;

class Parent {
    // 调用父类的静态代码块
    final static int A = 2222;
    // 全局一份的成员变量，可以考虑安全初始化
    static Son s = null;

    static {
        s = new Son();
        System.out.println("expect...");
    }

}

class Son extends Parent {
    public static int B = A;
}

// 主动引用
public class initiative {


    public static void main(String[] args) {
//        System.out.println(Son.B); // 2
//        System.out.println(Son.A);//0
        System.out.println(Parent.s.B);

    }
}
