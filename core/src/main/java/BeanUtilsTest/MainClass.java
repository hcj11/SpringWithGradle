package BeanUtilsTest;

class OuterClass {
    private static String msg = "hello world";

    class innerClass {
        public void display() {
            System.out.println("====�ڲ���������þ�̬�ͷǾ�̬����===" + msg);
        }
    }

    static class staticInnerClass {
        public void display() {
            System.out.println("====��̬�ڲ���ֻ�������þ�̬����===" + msg);
        }
    }
}

public class MainClass {
    public static void main(String[] args) {

        OuterClass outerClass = new OuterClass();
        // �ڲ���
        OuterClass.innerClass innerClass = outerClass.new innerClass();
        innerClass.display();
        // ��̬�ڲ���
        OuterClass.staticInnerClass staticInnerClass = new OuterClass.staticInnerClass();
        staticInnerClass.display();

    }
}
