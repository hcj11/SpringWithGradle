package boot.logging.logback;

import boot.logging.SpringBootLoggingConfiguration;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.util.StatusPrinter;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import com.alibaba.nacos.client.logging.logback.LogbackNacosLogging;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

@Slf4j
public class LogbackTest {
    Logger logger = LoggerFactory.getLogger(LogbackTest.class);
    @Test
    public void test1() {
        Logger logger = LoggerFactory.getLogger("boot.logging.logback.LogbackTest");
        logger.debug("hello world");
    }

    @Test
    public void test2() {
        LoggerContext iLoggerFactory = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(iLoggerFactory);
    }

    @Test
    public void test3() {
        // root default is debug  level
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(LogbackTest.class);
        logger.setLevel(Level.INFO);
        logger.warn("waring!!! , fire in the house");
        logger.info("info is welcomed ");
        logger.debug("debug not pass around ");
    }

    @Test
    public void test4() {
        // 树结构继承关系，子类找最近的父类
        ch.qos.logback.classic.Logger logger1 = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("boot.logging");
        logger1.setLevel(Level.INFO);

        ch.qos.logback.classic.Logger logger2 = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("boot.logging");
        logger2.warn("waring!!! , fire in the house");
        logger2.info("info is welcomed ");
        logger2.debug("", "debug not pass around");
    }

    @Test
    public void test5() {
        Object[] newArray = {"newVal", "below", "above"};
        log.info("{},be inserted between {} and  {}", newArray);
    }

    // root 级别 OFF  相关的类都会不打印。  effective level > request level
    @Test
    public void test6() {
        System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "logback-test.xml");
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(LogbackTest.class);

        ch.qos.logback.classic.Logger logger1 = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(SpringBootLoggingConfiguration.class);
        logger.info("hello ,world");
        logger1.info("hello , wolrd  said  by the springbootlogging");
    }

    @Test
    public void test7() throws IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        log.info("classloader========={}",cl.toString());
        org.springframework.core.io.ClassPathResource classPathResource =   new org.springframework.core.io.ClassPathResource("application.properties");
        BufferedReader reader = FileUtil.getReader(classPathResource.getFile(), Charset.defaultCharset());
        Properties properties = new Properties();
        properties.load(reader);
        properties.list(System.out);
    }

    @Test
    public void test8(){
        // TimeBasedFileNamingAndTriggeringPolicy
//        new TimeBasedTriggeringPolicy();
//            TimeBasedRollingPolicy
        //PatternLayoutEncoder
//        new PatternLayoutEncoder();
//        new PatternLayout();
        /**
         * GEventEvaluator is a concrete EventEvaluator implementation taking arbitrary Groovy language
         * boolean expressions as the evaluation criteria
         */
    }
    @Test
    public void test9(){
        MDC.put("first","h");
        MDC.put("last","cj");
        log.info(" is my name");
        MDC.put("first","h1");
        MDC.put("last","cj2");
        log.info(" is my children name");
    }
    @Test
    public void test10(){
        RuntimeException intend = new RuntimeException("intend");
        log.warn("{}","111",intend);
        intend.printStackTrace();
        logger.warn("i am stuck",intend);

        Instant plus = Instant.now().plus(365 * 24 , ChronoUnit.HOURS);
        System.out.println(plus);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime plus1 = LocalDateTime.now().plus(365 * 24, ChronoUnit.HOURS);
        String format = plus1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int nano = Instant.now().getNano();
        String format1 = LocalDateTime.now().format(dateTimeFormatter);
        log.info("{},{},{}",format,nano,format1);
    }
    @Test
    public void test11(){
        //
    }

}
