package BeanUtilsTest;

class Parent {
    // ���ø���ľ�̬�����
    final static int A = 2222;
    // ȫ��һ�ݵĳ�Ա���������Կ��ǰ�ȫ��ʼ��
    static Son s = null;

    static {
        s = new Son();
        System.out.println("expect...");
    }

}

class Son extends Parent {
    public static int B = A;
}

// ��������
public class initiative {


    public static void main(String[] args) {
//        System.out.println(Son.B); // 2
//        System.out.println(Son.A);//0
        System.out.println(Parent.s.B);

    }
}
