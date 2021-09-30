package ParametersizedType;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * 方法重载 和 泛型擦除
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

    // 而存储容器，却可以保存修改，因为保存到堆中的引用。和基本数据类型区分开，保存的位置是不同的
    public void sayHello(final List<String> aas) {
        aas.add("2");
    }

    //  直接复制一份到jvm方法栈中了
    public void sayHello(String aa) {
        aa = "33";
    }
    public void sayHello(StringBuffer aas) {
       aas.append("2");
    }

//    public void sayHello(int aa) {
//        aa = 44;
//    }
// 一定要明确jvm的运行时数据区的结构，  放到jvm方法栈中，
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
