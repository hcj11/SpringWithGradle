package BeanUtilsTest;

public class Demo {
    static class Parent {
        static {
            System.out.println("初始化class 的clinit方法");
        }

        // final 修饰的变量，在验证环境就已经存入到常量池中
         static int A = 1;

    }
    //
    static class Son extends Parent {
        static { // 代码块的赋值 ，前后顺序，先执行B=3->
            B = 3;
            System.out.println("初始化子类的class 的clinit方法");
        }

        static int B = A; // 类的初始化，先初始化父类clinit 方法。
    }
    // 先使用parent 下的变量，后是子类的、
    public static void main(String[] args) {
        System.out.println(Son.A);// 直接
    }
}

