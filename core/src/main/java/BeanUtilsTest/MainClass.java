package BeanUtilsTest;

class OuterClass {
    private static String msg = "hello world";

    class innerClass {
        public void display() {
            System.out.println("====内部类可以引用静态和非静态属性===" + msg);
        }
    }

    static class staticInnerClass {
        public void display() {
            System.out.println("====静态内部类只可以引用静态属性===" + msg);
        }
    }
}

public class MainClass {
    public static void main(String[] args) {

        OuterClass outerClass = new OuterClass();
        // 内部类
        OuterClass.innerClass innerClass = outerClass.new innerClass();
        innerClass.display();
        // 静态内部类
        OuterClass.staticInnerClass staticInnerClass = new OuterClass.staticInnerClass();
        staticInnerClass.display();

    }
}
