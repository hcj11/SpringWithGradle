package other;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.springframework.util.Assert;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.ServiceLoader;

@JSONType(serializer = String.class)
@NoArgsConstructor
@Data
class Fruit{
}
@NoArgsConstructor
@AllArgsConstructor
@Data
class Apple extends Fruit{

    private Double price;
    private String name;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss",serialzeFeatures = {
            SerializerFeature.WriteClassName,
            SerializerFeature.IgnoreErrorGetter
        }
            )
    private LocalDateTime createTime;

    @JSONField(serialzeFeatures = {SerializerFeature.WriteBigDecimalAsPlain})
    private BigDecimal decimal;
}
@NoArgsConstructor
@AllArgsConstructor
@Data
class Store{
    private Fruit fruit;
}
public class JsonDemo {
    public static void demo1() {
        HashMap hashMap = new HashMap();
        hashMap.put("1", Lists.<String>newArrayList("2", "3"));
        double dd = 1.1;
        BigDecimal bigDecimal1 = new BigDecimal(dd);
        BigDecimal bigDecimal2 = new BigDecimal("1.1");
        BigDecimal bigDecimal3 = new BigDecimal("1.11");
        System.out.println(bigDecimal1);
        System.out.println(bigDecimal2);
        System.out.println(bigDecimal3);
//        Assert.isTrue(bigDecimal1.compareTo(bigDecimal2)!=0,"错误");
        // 不在乎精度。 1.1 和1.10 是相等
        Assert.isTrue(bigDecimal3.compareTo(bigDecimal2) == 0, "错误");
    }

    public static void demo2()throws IOException, ClassNotFoundException {
        Transformer transformer = new InvokerTransformer<>("exec", new Class[]{String.class},
                new Object[]{"C:\\Windows\\System32\\calc.exe"});
        OutputStream outputStream = Files.newOutputStream(Paths.get("F:\\integration\\basic\\JVMDemo\\src\\main\\resources\\test.txt"), StandardOpenOption.WRITE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(transformer);

        InputStream inputStream = Files.newInputStream(Paths.get("F:\\integration\\basic\\JVMDemo\\src\\main\\resources\\test.txt"), StandardOpenOption.READ);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Transformer transformer1 = (Transformer) objectInputStream.readObject();

        transformer1.transform(Runtime.getRuntime());
    }
    public static void demo3(){
        Store store = new Store(new Apple(1.0d,"香蕉",LocalDateTime.now(),new BigDecimal("1")));
        String jsonString = JSON.toJSONString(store,SerializerFeature.WriteClassName);
        System.out.println(jsonString);

        Store store1 = JSON.parseObject(jsonString, Store.class);
        System.out.println(store1.getFruit());
        System.out.println((Apple)store1.getFruit());
    }
    public static void main(String[] args)  {
        demo3();

    }
}
