package BeanUtilsTest;

public class StringTest2 {
    public String aaa = "hello";
    public static String cccc = "dsa";
    // final���εĳ��� �� �ַ�������
    public static String dddd = "";
    public final static int eeee = 2; // Ĭ��ʹ�ð�װ���� ����int  #18 = Integer            2


    private void hello() {
        String bbb = "world";
    }

    public static void main(String[] args) {
        // jdk1.8 �� �ַ��������ػ��ڶ�, ����ʱ�����ػ��ڷ����� ��
        // �ڹ�������в������ɡ�abcdef"�ַ���������ԭ�� δ�����ģ�ֻ�Ž�����Ѿ������ģ�ֻ������
//        String s = new String("abc") + new String("def");
//        System.out.println(s);
//        String s1 = new StringBuilder(1024).append("hello").append("world").toString();
//        System.out.println(s1);
//        String s1 = "he" + "world"; // �Ż��� StringBuilder , ldc ���������еı����Ƶ�ջ����
//        System.out.println(s1);
        // 0.��Ա���������ڼ��س�ʼ���׶Σ�ʹ�õ����ַ���������,�����ر���������ʹ��ʱ����ʹ�õ��ַ���������
        // 1.jdk1.8 �� String.intern() ����������ʱ���ڼ���ַ����ŵ��ַ�����������

        String sss = new String("111");// ��,���س�����(Ҳ�Ὠ��һ��) ��Ϊָ��ͬ�����Բ�ͬ��
        String sss2 = "111"; // ʹ�ñ��ر���������
        String sss3 = "111"; //
        System.out.println(sss==sss2); //
        System.out.println(sss3==sss2); //��ʹ�ñ��ر��������ã�����ֻ��һ�ݡ�
        String sss4 = new String("111");
        System.out.println(sss==sss4); // ֻ����һ�����󣬶���Ӷ�����ö���

    }
}

