package Others;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

public class ReferenceDemo {
    public static void demo() throws InterruptedException {
        Object obj = new Object();
        ReferenceQueue<Object> refQueue = new ReferenceQueue<>();
        PhantomReference<Object> phanRef = new PhantomReference<>(obj, refQueue);

        Object objg = phanRef.get();
        //�����õ�����null
        System.out.println(objg);
        //��obj�������
        obj = null;
        System.gc();
        Thread.sleep(3000);
        //gc��ὫphanRef���뵽refQueue��
        Reference<? extends Object> phanRefP = refQueue.remove();
        //�������true
        System.out.println(phanRefP==phanRef);

    }

    public static void main(String[] args) throws InterruptedException {
        demo();
    }
}
