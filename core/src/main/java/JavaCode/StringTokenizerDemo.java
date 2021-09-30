package JavaCode;


import lombok.Data;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum Types {
    A, B;

    @Override
    public String toString() {
        return super.toString();
    }
}

public class StringTokenizerDemo {
    static final int maxFiled = 5;
    static final String delimi = ", |";

    public static void demo2() {
        StringTokenizer stringTokenizer = new StringTokenizer("hello, world|of|java", delimi, true);
        while (stringTokenizer.hasMoreElements()) {
            System.out.println(stringTokenizer.nextElement());
            ;
        }
        ;
        System.out.println("=================================");
        StringTokenizer stringTokenizer2 = new StringTokenizer("hello, world|of|java", delimi, false);
        while (stringTokenizer2.hasMoreElements()) {
            System.out.println(stringTokenizer2.nextElement());
            ;
        }
        ;
    }

    public static void main(String[] args) {
        demo11();
    }

    /**
     * 识别中间的    业务分隔符
     */
    public static void demo1() {
        StringTokenizer stringTokenizer = new StringTokenizer("hello,  |world|of|java", delimi, false);
        while (stringTokenizer.hasMoreElements()) {
            System.out.println(stringTokenizer.nextElement());
            ;
        }
        ;
    }

    /**
     *
     */
    public static void demo3() {
        //hello,  |world|of|java
        String[] strings = new String[4];
        int i = 0;
        StringTokenizer stringTokenizer = new StringTokenizer("hello|||java", "|", true);
        while (stringTokenizer.hasMoreElements()) {
            String s = stringTokenizer.nextToken();
            if (s.equals("|")) {
                if (i++ >= maxFiled) {
                    throw new IllegalArgumentException("超过最大值");
                }
                continue;
            }
            strings[i] = s;
        }
        Arrays.stream(strings).forEach(System.out::println);
    }

    public static void demo4() {
        StringBuilder stringBuilder = new StringBuilder();
        StringTokenizer hello_world = new StringTokenizer("hello world of java");
        while (hello_world.hasMoreElements()) {
            String s = hello_world.nextToken();
            stringBuilder.append(s);
            if (hello_world.hasMoreElements()) {
                stringBuilder.append(", ");
            }
        }
        System.out.println(stringBuilder);
    }

    public static void demo5() {
        Types types = Types.A;
        switch (types) {
            case A:
                break;
            case B:
                break;
            default:
                break;
        }
    }

    public static void demo6() {
        Stack<String> strings = new Stack<>();
        strings.push("");
        while (!strings.empty()) {
            strings.pop();
        }
    }

    public static void demo7() {
        String tmpstr = "\"hello wolrd\", 123, \"dsd\", 456,  , ";
        Pattern compile = Pattern.compile("\"([^\"]+?)\", ?|([^, ]+), ?|, ");

        Matcher matcher = compile.matcher(tmpstr);
//        if(matcher.matches()){
//            String group1 = matcher.group();
//            System.out.println(group1);
//        }

        while (matcher.find()) {
            String group = matcher.group();
            System.out.println(group);

        }
        matcher.reset();

    }

    public static void demo8() throws IOException {
        Properties properties = new Properties(null);
        properties.load(Files.newInputStream(Paths.get(""), StandardOpenOption.READ));

        try {
            properties.load(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        properties.getProperty("");
        properties.get("");
        properties.list(System.out);
    }

    public static void demo9()  {
        TreeSet<String> strings = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        strings.add("J");
        strings.add("a");
        strings.add("e");
        strings.add("d");
        strings.add("d");
        // [}
         // 获取d之后的元素  dej
        System.out.println(strings.tailSet("d"));
        System.out.println(strings.headSet("a"));
        System.out.println("=================================================");
        System.out.println(strings.first());
        System.out.println("=================================================");
        strings.stream().forEach(System.out::println);
    }
    public static void demo10()  {
        A a = new A("123");
        A aclone = new A(a);
        System.out.println(aclone);
        // integer not do setvalue
        Integer integer = new Integer(1);
        integer= new Integer(2);

    }
    public static void demo11()  {
        char a='a';
        int a1 = a;
        System.out.println(a1);
    }

    @Data
    static class A{
        private  String name;
        public A(){}
        public A(A a){this.name=a.name;}
        public A(String name){this.name=name;}
    }

}