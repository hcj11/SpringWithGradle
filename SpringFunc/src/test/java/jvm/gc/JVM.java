package jvm.gc;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.scopedpool.SoftValueHashMap;
import org.junit.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

@Slf4j
class CustomMap<K, V> extends HashMap<K, V> {
    private CustomMap customMap;
    @Override
    protected void finalize() throws Throwable {
        log.error("CustomMap .. gc");
        /**
         * ����gc
         */
        this.customMap =  this;
    }
    //    private String var;
//
//    @Override
//    protected void finalize() throws Throwable {
//        if (var == null) {
//            log.error("gc collection is running !!!");
//            var = "yes , i am Survived!!!";
//        }
//    }
}

@Slf4j
class CustomPhantomReference<T> extends PhantomReference<T> {

    /**
     * Creates a new phantom reference that refers to the given object and
     * is registered with the given queue.
     *
     * <p> It is possible to create a phantom reference with a <tt>null</tt>
     * queue, but such a reference is completely useless: Its <tt>get</tt>
     * method will always return null and, since it does not have a queue, it
     * will never be enqueued.
     *
     * @param referent the object the new phantom reference will refer to
     * @param q        the queue with which the reference is to be registered,
     */
    public CustomPhantomReference(T referent, ReferenceQueue<? super T> q) {
        super(referent, q);
    }

    @Override
    protected void finalize() throws Throwable {
        log.error("{},gc collection is running !!!", "CustomPhantomReference");
    }
}

@Slf4j
public class JVM {
    @Test
    public void MemStructure(){
        /**
         * �˴���s3 ָ������õ�ַ
         */
        String s3=new String("helloworld");
        String intern = s3.intern();
        String s4="helloworld";
        log.info("s3:{},s4:{},{},intern:{}",s3.hashCode(),s4.hashCode(),s3==s4,intern==s4);
        Assert.isFalse(s3==s4);
        /**
         * �˴���s1 ָ������õ�ַ
         */
        String s1=new String("hello") + new String("world");
        s1.intern();
        String s2="helloworld";
        log.info("s1:{},s2:{},{},intern:{}",s1.hashCode(),s2.hashCode(),s1==s2,s1.intern()==s2);

        Assert.isFalse(s1==s2);


    }
    /**
     * PhantomReference ��Ķ����޷������á�
     */
    @Test
    public void PhantomReference() throws InterruptedException {
        ReferenceQueue<Object> customQueue = new ReferenceQueue<>();

        CustomMap<String, String> map = new CustomMap<String, String>() {{
            put("helo", "�»���");
            put("world", "������");
        }};
        CustomPhantomReference<Map<String, String>> mapPhantomReference = new CustomPhantomReference<>(map, customQueue);
        boolean enqueue = mapPhantomReference.enqueue();
        boolean enqueued = mapPhantomReference.isEnqueued();
        Assert.isTrue(enqueue == enqueued);

        /**
         * waitting for invoke gc
         * δ����gc
         */
        map = null;
        mapPhantomReference=null;
        Runtime.getRuntime().gc();
        Runtime.getRuntime().runFinalization();
        long l1 = Runtime.getRuntime().totalMemory();
        long l2 = Runtime.getRuntime().freeMemory();
        long l3 = Runtime.getRuntime().maxMemory();
        log.info("{},{},{}",l1,l2,l3);

        while (true){}
    }

    /**
     * ��gcʱ���л��ա� ����ֱ��ѡ�������ý��л���
     */
    @Test
    public void weakReference() throws InterruptedException {
        WeakHashMap<String, Object> objectObjectWeakHashMap = new WeakHashMap<>();
        objectObjectWeakHashMap.put("recreate", "yes");
        Object recreate = objectObjectWeakHashMap.remove("recreate");
        Assert.isTrue(recreate.equals("yes"));
        Object put = objectObjectWeakHashMap.put("recreate", "yes_recreate");
        Assert.isTrue(put == null && objectObjectWeakHashMap.get("recreate").equals("yes_recreate"));

        /**
         * ��ʾ����ɾ��key�� ����ָ��helo=null(�ͱ�ʾgc���Խ��л��ա�)  gc ���л���ɾ��key
         */
        objectObjectWeakHashMap.put("helo", "yes, it is !!!");
        Object helo = objectObjectWeakHashMap.get("helo");
        helo = null;
        Assert.isTrue(objectObjectWeakHashMap.containsKey("helo"));


        HashMap<String, String> map = new HashMap<String, String>() {{
            put("helo", "�»���");
            put("world", "������");
        }};
        map.remove("helo");

        WeakReference<Map<String, String>> mapWeakReference = new WeakReference<>(map);
        boolean enqueue = mapWeakReference.enqueue();
        Map<String, String> map1 = mapWeakReference.get();
        boolean ued = mapWeakReference.isEnqueued();
        Assert.isTrue(enqueue == ued);
        Assert.isTrue(map1 != null && map1.get("world").equalsIgnoreCase("������"));

        String world = map1.get("world");
        world=null;

        Thread.sleep(1000000);


    }

    /**
     * ��gcʱ���л��ա� ͨ���㷨���л��ա�
     */
    @Test
    public void softReference() {
        SoftValueHashMap softValueHashMap = new SoftValueHashMap();
        softValueHashMap = null;

        Map<String, String> map = new HashMap<String, String>() {{
            put("helo", "�»���");
            put("world", "������");
        }};
        SoftReference<Map<String, String>> mapSoftReference = new SoftReference<>(map);
        boolean enqueue = mapSoftReference.enqueue();
        mapSoftReference.isEnqueued();
        if (mapSoftReference.get() == null) {
            log.info("have been gc !!!");
        }

    }
}
