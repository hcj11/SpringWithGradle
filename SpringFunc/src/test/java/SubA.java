import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

@Qualifier
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
 public @interface SubA{
    String value() default "";
}

