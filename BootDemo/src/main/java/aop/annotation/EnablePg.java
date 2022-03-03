package aop.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * sql
 */
@Target(value={METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnablePg {
    String value() default "";
}
