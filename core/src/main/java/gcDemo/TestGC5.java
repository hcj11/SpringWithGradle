package gcDemo;

public class TestGC5 {
    private static final int _1MB = 1024 * 1024;

    /**
     * // Error: VM option 'PromotionFailureALot' is notproduct and is available only in debug version of VM.
     *
     * VM������-Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:-HandlePromotionFailure
     */
    @SuppressWarnings("unused")
    public static void testHandlePromotion() {
        // ȡ�����䵣������ָ�full gc������飬������뵽��������
        byte[] allocation1, allocation2, allocation3, allocation4, allocation5, allocation6, allocation7;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        allocation1 = null;
        allocation4 = new byte[2 * _1MB];
        allocation5 = new byte[2 * _1MB];
        allocation6 = new byte[2 * _1MB];
        allocation4 = null;
        allocation5 = null;
        allocation6 = null;
        allocation7 = new byte[2 * _1MB];
    }


    public static void main(String[] args) {
        testHandlePromotion();
    }
}
