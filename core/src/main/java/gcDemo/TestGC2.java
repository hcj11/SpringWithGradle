package gcDemo;

public class TestGC2 {
    private static final int _1MB = 1024 * 1024;

    /**
     * VM������-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
     * -XX:PretenureSizeThreshold=3145728   todo δֱ�ӵ���������
     */
    public static void testPretenureSizeThreshold() {
        byte[] allocation;
        allocation = new byte[4 * _1MB];  //ֱ�ӷ������������
    }

    public static void main(String[] args) {
        testPretenureSizeThreshold();
    }
}
