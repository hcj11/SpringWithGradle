import org.junit.jupiter.api.Test;

public class CaseTest {
    public static class X {
        // new ,class �������г�ʼ�� set,get ����δ��ʼ���Ļ�
        private String name = "black";
        static Z y = new Z();
    }

    public static class Y extends X {

    }

    public static class Z extends X{
        private String name = "red";
    }
    @Test
    public void test1(){

    }


}
