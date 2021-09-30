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

        { // 在实例初始化过程中，后置赋值
            listsssssssssssssssssssssssssss.add("1ssssssssssssssssssss2");
            listsssssssssssssssssssssssssss.add("1sssssssssssssssssssss3");
        }
    }

    public static void main(String[] args) {

        String str1 = new StringBuilder("哈哈").append("n").toString();

        String str2 = new StringBuilder("ja").append("va").toString();

        System.out.println(str1 == str1.intern()); // true

        System.out.println(str2.intern() == str2); // false
//
//        String s = new String("hew就"); // 堆和常量池中都存在一份引用，
//        s.intern();
//        String s2 = "hew就"; // 由于直接
//        System.out.println(s == s2.intern()); //false
//        String s3 = new String("hew就");
//        System.out.println(s==s3); // false 建立多份引用，导致不相等
//        System.out.println(s==s2); // false 建立多份引用，导致不相等


        // 而 StringBuilder 和 StringBuffer则是存储到堆中， 需要区分开
        // 常量池中只会存储一份， 而并不在堆上， 并且常量池中也有大小限制。
        // 而对于数组和字符串的String类型则是存储到堆中的
//        String aaa = new StringBuilder(1024).append("hello world计算器").toString();
//        String bbb = new StringBuilder().append("计算器").toString();
//        String sss = new String("hello world计算器");
//        String sss2 = new String("hello world计算器");
//        String ccc = new StringBuilder(1024).append("ja").append("va").toString();
//        System.out.println(sss==sss2); // -> false ,只不过是指向原对象相同的引用而已。
//        System.out.println(bbb.intern()==bbb.intern());
//        StringBuffer 是线程安全的  vs StringBuilder 线程不安全。
        // aaa.intern() 为每个唯一的字符串序列生成一个且仅有一个String 引用
//        String intern = aaa.intern();
        // aaa.intern()操作 从常量池中获取 已经存在的数据，并且提取、
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
