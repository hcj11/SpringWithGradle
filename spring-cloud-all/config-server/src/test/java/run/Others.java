package run;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.ClassUtils;

public class Others {
    @Test
    public void test(){
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        String name = HikariDataSource.class.getName();
        Assertions.assertTrue(ClassUtils.isPresent(name,contextClassLoader));
    }
}
