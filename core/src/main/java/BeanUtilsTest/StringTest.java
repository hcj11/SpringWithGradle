package BeanUtilsTest;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class StringTest {
    @Getter
    @Setter
    class A {
        private List<String> listsssssssssssssssssssssssssss = new ArrayList<String>();

        { // ��ʵ����ʼ�������У����ø�ֵ
            listsssssssssssssssssssssssssss.add("1ssssssssssssssssssss2");
            listsssssssssssssssssssssssssss.add("1sssssssssssssssssssss3");
        }
    }

    public static void main(String[] args) {

        String str1 = new StringBuilder("����").append("n").toString();

        String str2 = new StringBuilder("ja").append("va").toString();

        System.out.println(str1 == str1.intern()); // true

        System.out.println(str2.intern() == str2); // false
//
//        String s = new String("hew��"); // �Ѻͳ������ж�����һ�����ã�
//        s.intern();
//        String s2 = "hew��"; // ����ֱ��
//        System.out.println(s == s2.intern()); //false
//        String s3 = new String("hew��");
//        System.out.println(s==s3); // false ����������ã����²����
//        System.out.println(s==s2); // false ����������ã����²����


        // �� StringBuilder �� StringBuffer���Ǵ洢�����У� ��Ҫ���ֿ�
        // ��������ֻ��洢һ�ݣ� �������ڶ��ϣ� ���ҳ�������Ҳ�д�С���ơ�
        // ������������ַ�����String�������Ǵ洢�����е�
//        String aaa = new StringBuilder(1024).append("hello world������").toString();
//        String bbb = new StringBuilder().append("������").toString();
//        String sss = new String("hello world������");
//        String sss2 = new String("hello world������");
//        String ccc = new StringBuilder(1024).append("ja").append("va").toString();
//        System.out.println(sss==sss2); // -> false ,ֻ������ָ��ԭ������ͬ�����ö��ѡ�
//        System.out.println(bbb.intern()==bbb.intern());
//        StringBuffer ���̰߳�ȫ��  vs StringBuilder �̲߳���ȫ��
        // aaa.intern() Ϊÿ��Ψһ���ַ�����������һ���ҽ���һ��String ����
//        String intern = aaa.intern();
        // aaa.intern()���� �ӳ������л�ȡ �Ѿ����ڵ����ݣ�������ȡ��
//        System.out.println(intern == aaa.intern()); // true
//        System.out.println(intern == bbb.intern()); // true
//        System.out.println(intern == sss.intern()); // true
//        System.out.println(ccc.intern()==ccc.intern()); // false
//        StringTest stringTest = new StringTest();
//        A instance = stringTest.getInstance();

    }

    private A getInstance() {
        return new A();
    }
}
