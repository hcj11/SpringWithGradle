package BeanUtilsTest;

public class Test {
    static {
        i = 0; // ���ǿ���δ�ñ�����ֵ�� ���ǰ����Ⱥ�˳�����ո�ֵΪ1
//        System.out.println(i); ���Ϸ�����ǰ����
    }
// statis ֻ�ܷ��ʵ���������֮ǰ�� ����
    static int i = 1;
    // final ���ε� �Ѿ�λ�� ���� �У��������������ط�
//    static final String s=new String("111");
    // ����final���εĳ�Ա����������Ҳλ�ڳ�������
//
//    StringBuilder stringBuilder = new StringBuilder(1024).append("hello world");
//    String s=new String("111");

    public static void main(String[] args) {
        System.out.println(Test.i);

    }
}
