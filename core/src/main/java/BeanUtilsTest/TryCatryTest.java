package BeanUtilsTest;

public class TryCatryTest {
    /**
     * �����try catch ��return �� ����
     * �ֽ���Ƕ���������
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
            return x;  // ireturn ���ã��ǽ�returnValue�е�ֵ���Ƶ���ջ���������ء�
        }
    }

    public static void main(String[] args) {
        System.out.println(inc());
    }
}
