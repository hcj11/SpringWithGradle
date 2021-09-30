package aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * ·Ö¶Îsql
 */
//@Target({ElementType.PARAMETER,ElementType.FIELD})
@Target(value={CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrgCode {
    String value() default "";
}
