package aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;


@Slf4j
public class Demo1 {
    public static void demo4(AnnotationConfigApplicationContext context) {
        Shopping bean = (Shopping) context.getBean(Shopping.class);
        bean.addColsWithMap(null, new HashMap(16, 0.75f) {{
            put("i", "goit");
        }});
    }

    public static void demo3(AnnotationConfigApplicationContext context) {
        Shopping bean = (Shopping) context.getBean(Shopping.class);
        bean.addColsWithString(null);
    }

    public static void demo2(AnnotationConfigApplicationContext context) {
        Using customUsing = (Using) context.getBean("userService");
        customUsing.use();
    }

    public static void demo1(AnnotationConfigApplicationContext context) {
        Shopping bean = (Shopping) context.getBean(Shopping.class);
        try {
            bean.showMap(new HashMap() {
                {
                    put("hello", "world");
                }
            });

        } catch (Exception e) {
            log.error("=调用者进行异常处理========={}", e.getMessage());
        }
    }

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CustomAopConfig.class);
        demo4(context);


    }

}
