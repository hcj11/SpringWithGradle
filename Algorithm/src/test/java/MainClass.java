import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class OuterClass {
    private static String msg = "hello world";
    public String showArray(Object... args){
        return "hcj";
    }

    class innerClass {
        public void display() {
            System.out.println("+" + msg);
        }
    }

    static class staticInnerClass {
        public void display() {
            System.out.println("=" + msg);
        }
    }
}

public class MainClass {
    @Test
    public void test2() throws Exception {
        OuterClass outerClass = new OuterClass();
        Method showArray = OuterClass.class.getDeclaredMethod("showArray", Object[].class);
        Object invoke = showArray.invoke(outerClass,new Object[]{null});
        Assertions.assertTrue(((String)invoke).equals("hcj"));

    }
    @Test
    public void test1(){
        OuterClass outerClass = new OuterClass();
        OuterClass.innerClass innerClass = outerClass.new innerClass();
        innerClass.display();
        OuterClass.staticInnerClass staticInnerClass = new OuterClass.staticInnerClass();
        staticInnerClass.display();
    }
}
