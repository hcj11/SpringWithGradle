package ParametersizedType;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * �������� �� ���Ͳ���
 */
public class OverloadParametypesized {
//    public static String method(List<String> aaa) {
//        return "";
//
//    }
//
//    public static int method(List<Integer> aaa) {
//        return 1;
//    }

    static class A {
        // 'method(ArrayList<String>)' clashes with 'method(ArrayList<Integer>)'; both methods have same erasure
//        public void method(ArrayList<String> aaa){
//
//        }
//        public void method(ArrayList<Integer> aaa){
//
//        }
        // 'method(List<String>)' clashes with 'method(List<Integer>)'; both methods have same erasure


    }

    // ���洢������ȴ���Ա����޸ģ���Ϊ���浽���е����á��ͻ��������������ֿ��������λ���ǲ�ͬ��
    public void sayHello(final List<String> aas) {
        aas.add("2");
    }

    //  ֱ�Ӹ���һ�ݵ�jvm����ջ����
    public void sayHello(String aa) {
        aa = "33";
    }
    public void sayHello(StringBuffer aas) {
       aas.append("2");
    }

//    public void sayHello(int aa) {
//        aa = 44;
//    }
// һ��Ҫ��ȷjvm������ʱ�������Ľṹ��  �ŵ�jvm����ջ�У�
    public void sayHello(Integer aa) {
        aa = 44;
    }

    public static void main(String[] args) {
        String aa = "22";
        OverloadParametypesized overloadParametypesized = new OverloadParametypesized();
        overloadParametypesized.sayHello(aa);
        System.out.println(aa);
        ArrayList<String> strings = Lists.newArrayList("1");
        overloadParametypesized.sayHello(strings);
        System.out.println(strings);
        int aaa = 4;
        overloadParametypesized.sayHello(aaa);
        System.out.println(aaa);
        Integer bbb =5;
        overloadParametypesized.sayHello(bbb);
        System.out.println(aaa);
        String aa2 = new String("23");
        overloadParametypesized.sayHello(aa2);
        System.out.println(aa2);
        StringBuffer stringBuffer = new StringBuffer("1");
        overloadParametypesized.sayHello(stringBuffer);
        System.out.println(stringBuffer);


    }
}
