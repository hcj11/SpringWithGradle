package ParametersizedType;

public class Test1 {
    public static void test2(){
        // ���� Byte��Short��Integer��Long��Character ��5�����͵İ�װ��Ҳֻ���ڶ�Ӧֵ�� [-128,127] ʱ�Ż�ʹ�û���أ�
        // �����˷�Χ��Ȼ��ȥ�����µĶ���
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
        // ��װ��ġ�==�� �����ڲ������������������²����Զ����䡣�Լ����ǵ�equals��������������ת�͵Ĺ�ϵ�� �������������ѯ��װ��
    }
    public static void test1(){
        // �������� ���ж��Ƿ���ȡ�
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
