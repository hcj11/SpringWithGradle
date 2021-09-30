package BeanUtilsTest;

public class TryCatryTest {
    /**
     * 经典的try catch 和return 的 场景
     * 字节码角度来分析，
     */
    public static int inc() {
        int x = 0;
        x = 10;
        try {
            x = 1;
//            int y=1/0;
            return x;
        } catch (Exception e) {
            x = 2;
            return x;
        } finally {
            x = 31;
            return x;  // ireturn 作用，是将returnValue中的值，推到到栈顶，并返回。
        }
    }

    public static void main(String[] args) {
        System.out.println(inc());
    }
}
