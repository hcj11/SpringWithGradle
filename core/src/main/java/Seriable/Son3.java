package Seriable;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


/**
 * 自定义obj的读写操作
 * ReadObject ,WriteObject
 */
@Getter
@Setter
public class Son3 implements Serializable {
    private static final long serialVersionUID = 42L;

    public Instant name;

    public Son3() {
    }

    public Son3(Instant name) {
        this.name = name;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(name.plus(1, ChronoUnit.DAYS));
        System.out.println("=====生成对象===");
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        name = (Instant) in.readObject();
        System.out.println("=====在实例构造之后，生成对象==="+name);
    }



    public static void main(String[] args) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream("F:\\integration\\basic\\JVMDemo\\src\\main\\java\\Seriable\\t.tmp");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        Son3 son = new Son3(Instant.now());
        son.writeObject(objectOutputStream);

        FileInputStream fileIn = new FileInputStream("F:\\integration\\basic\\JVMDemo\\src\\main\\java\\Seriable\\t.tmp");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileIn);
        son.readObject(objectInputStream);

    }
}
