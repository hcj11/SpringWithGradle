package BeanUtilsTest;

public class Demo {
    static class Parent {
        static {
            System.out.println("��ʼ��class ��clinit����");
        }

        // final ���εı���������֤�������Ѿ����뵽��������
         static int A = 1;

    }
    //
    static class Son extends Parent {
        static { // �����ĸ�ֵ ��ǰ��˳����ִ��B=3->
            B = 3;
            System.out.println("��ʼ�������class ��clinit����");
        }

        static int B = A; // ��ĳ�ʼ�����ȳ�ʼ������clinit ������
    }
    // ��ʹ��parent �µı�������������ġ�
    public static void main(String[] args) {
        System.out.println(Son.A);// ֱ��
    }
}

