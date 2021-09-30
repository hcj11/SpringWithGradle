package Jasypt;

import cn.hutool.core.lang.Assert;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import context.CompontScan;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
@PropertySource(value = {"classpath:/application.properties"})
@Configuration
@EnableEncryptableProperties
class Jasypt implements InitializingBean {
    static Path path = Paths.get("G:\\zkui\\允许创建用户.reg");
    static Path path_tmp = Paths.get("G:\\zkui\\允许创建用户.reg_tmp");
    static Path path_new  = Paths.get("G:\\zkui\\允许创建用户.reg_new");
    @Value("${jasypt.encryptor.password}")
    private String password;
    {
        Assert.isTrue(password==null);
    }
    public Jasypt(){
        Assert.isTrue(password==null);
    }
    @Test
    public void load(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Jasypt.class);

    }

    @SneakyThrows
    @Test
    public void encrypt(){
        new BigDecimal(100);

        String canonicalPath = path.toString();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Jasypt.class);
        CompontScan.print(context);
        StringEncryptor stringEncryptor = context.getBean(StringEncryptor.class);
        String initVal= new String(Files.readAllBytes(path));
        String encrypt = stringEncryptor.encrypt(initVal);
        Path write = Files.write(path_tmp, encrypt.getBytes()
                , StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        log.info("{}",write.getFileName().toString());
        decrypt();

    }
    @SneakyThrows
    @Test
    public void decrypt(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Jasypt.class);
        CompontScan.print(context);
        StringEncryptor stringEncryptor = context.getBean(StringEncryptor.class);
        byte[] bytes = Files.readAllBytes(path_tmp);
        String decrypt = stringEncryptor.decrypt(new String(bytes));
        Path write = Files.write(path_new, decrypt.getBytes()
                , StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        log.info("{}",write.getFileName().toString());
    }

    @Test
    public void demo2(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Jasypt.class);
        ConfigurableEnvironment environment = context.getEnvironment();
        String password = environment.getProperty("password");
        log.info("password:{}",password);

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.isTrue(password!=null);
    }
}

public class JasyptDemo {
}
