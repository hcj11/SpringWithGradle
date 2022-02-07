package scan;

import com.api.UserInterface;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ScopedProxyMode;
import utils.Utils;

public class CommonClassScan {
    /**
     just include the static  nest class;
     */
    @ComponentScan(
            useDefaultFilters = true,
            basePackages = "com.provider", scopedProxy = ScopedProxyMode.INTERFACES
    )
    public static class CustomA {

    }
    @ComponentScan(
            useDefaultFilters = false,
            includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = {UserInterface.class})},
            basePackages = "com.provider", scopedProxy = ScopedProxyMode.INTERFACES
    )
    public static class CustomB {

    }
    @Test
    public void scanA(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CustomA.class);
        Utils.print(context);
    }
    @Test
    public void scan(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CustomB.class);
        Utils.print(context);
        // NewUserInterfaceImpl

    }

}
