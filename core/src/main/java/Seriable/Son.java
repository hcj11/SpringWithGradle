package Seriable;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.Date;

class Parent {

}

@Getter
@Setter
public class Son extends Parent implements Serializable {
    private static final long serialVersionUID = 42L;

    public Date name;
    private String nameOther;
    public Son(){}
    public Son(Date name) {
        this.name = name;
    }

    public void ser() throws Exception {
        Son son = new Son(new Date());
        FileOutputStream fileOutputStream = new FileOutputStream("F:\\integration\\basic\\JVMDemo\\src\\main\\java\\Seriable\\t.tmp");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(son);
    }

    public void des() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("F:\\integration\\basic\\JVMDemo\\src\\main\\java\\Seriable\\t.tmp");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Son son1 = (Son) objectInputStream.readObject();
        Date name = son1.getName();
        fileInputStream.close();
        System.out.println("========" + name);
        System.out.println("========" + son1.getNameOther());
    }

    public static void main(String[] args) throws Exception {
        Son son = new Son();
        son.des();
//        son.des();


    }
}
