package Queue;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
class A implements Comparable<A> {
    private String name;
    private Integer age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        A a = (A) o;
        return Objects.equal(age, a.age)
                && Objects.equal(name, a.name);
    }

    @Override
    public int hashCode() {
        // hashMap Ĭ��ȡhashCode �ı��룬Ȼ�����������б�������ġ�
        return Objects.hashCode(age);
    }

    @Override
    public int compareTo(A b) {
        A a = this;
        if (a.getAge().compareTo(b.getAge()) == 0) {
            if (a.getName().compareTo(b.getName()) > 0) {
                return 1;
            } else if (a.getName().compareTo(b.getName()) == 0) {
                return 0;
            } else {
                return -1;
            }
        } else if (a.getAge().compareTo(b.getAge()) > 0) {
            return 1; // order
        } else {
            return -1;
        }
    }

}

public class CustomPriorityBlockingQueue<T extends A> extends PriorityBlockingQueue<A> {
    private static Comparator<A> comparator = (A a, A b) -> {
        if (a.getAge().compareTo(b.getAge()) == 0) {
            if (a.getName().compareTo(b.getName()) > 0) {
                return 1;
            } else if (a.getName().compareTo(b.getName()) == 0) {
                return 0;
            } else {
                return -1;
            }
        } else if (a.getAge().compareTo(b.getAge()) > 0) {
            return 1; // order
        } else {
            return -1;
        }
    };

//    public Queue.CustomPriorityBlockingQueue() {
//        super(5, comparator);
//    }

    public static void main(String[] args) {
    // �޷���֤ͬ���ȼ������˳��
        CustomPriorityBlockingQueue<A> customPriorityBlockingQueue = new CustomPriorityBlockingQueue<A>();
        customPriorityBlockingQueue.offer(new A("aaa", 4));
        customPriorityBlockingQueue.offer(new A("bbb", 1));
        customPriorityBlockingQueue.offer(new A("ccc", 3));
        customPriorityBlockingQueue.offer(new A("ddd", 0));
        System.out.println(customPriorityBlockingQueue.toString());
        customPriorityBlockingQueue.poll();

    }
}
