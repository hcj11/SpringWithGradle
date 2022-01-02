import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Demo {
    static StringBuffer stringBuffer = null;
    @BeforeEach
    public void setUp(){
         stringBuffer = new StringBuffer(1024);
    }
    @AfterEach
    public void unInstall(){
//        stringBuffer = new StringBuffer(1024);
    }

    static class Parent {
        static {
            stringBuffer.append("初始化class 的clinit方法;");
        }

        // final 修饰的变量，在验证环境就已经存入到常量池中
         static int A = 1;

    }
    static class Son extends Parent {
        static {
            B = 3;
            stringBuffer.append("初始化子类的class 的clinit方法");
        }

        static int B = A;
    }
    @Test
    public void test1(){
        org.junit.Assert.assertTrue(Son.A==1);
        org.junit.Assert.assertTrue(stringBuffer.toString().equals("初始化class 的clinit方法;"));
    }

    @Test
    public void test2(){//mock
        org.junit.Assert.assertTrue(Son.B==1);
        org.junit.Assert.assertTrue(stringBuffer.toString().equals("初始化class 的clinit方法;初始化子类的class 的clinit方法"));
    }

}

