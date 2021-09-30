package Seriable;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.Date;
@Setter
@Getter
class Parent2 {
    private Integer num;
}
@Getter
@Setter
class A  implements Serializable{
    private static final long serialVersionUID = 42L;
    private String orderId="hh";
    public A(String orderId){this.orderId=orderId;}
}

/**
 * 5. ?类继承 Serializable 接?，所有?类都可以被序列化。      yes
 * 6. ?类继承 Serializable 接?，?类没有继承，?类中的属性不能序列化。
 * 7. 如果序列化的属性是对象，这个对象也必须实现 Serializable 接?。  java.io.NotSerializableException: Seriable.A
 */
@Getter
@Setter
public class Son2 extends Parent2 implements Serializable {
    private static final long serialVersionUID = 42L;
    private Date name;
    private String nameOther;
    private A a;

    public Son2() {
    }

    public Son2(Date name) {
        this.name = name;
    }

    public void ser() throws Exception {
        Son2 son2 = new Son2(new Date());
        son2.setA(new A("1111"));
        FileOutputStream fileOutputStream = new FileOutputStream("F:\\integration\\basic\\JVMDemo\\src\\main\\java\\Seriable\\t.tmp");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(son2);

    }

    public void des() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("F:\\integration\\basic\\JVMDemo\\src\\main\\java\\Seriable\\t.tmp");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Son2 son2 = (Son2) objectInputStream.readObject();
        Date name = son2.getName();
        fileInputStream.close();
        System.out.println("========" + name);
        System.out.println("========" + son2.getNameOther());
        System.out.println("=====parent===" + son2.getNum());
        System.out.println("=====组合===" + son2.getA().getOrderId());
    }

    public static void main(String[] args) throws Exception {
        Son2 son = new Son2();
        son.ser();
        son.des();


    }
}
