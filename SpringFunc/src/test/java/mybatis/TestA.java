package mybatis;

import cn.hutool.core.annotation.AnnotationUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

//@EnabledIf("false")
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class Parent{
    Integer integer =null;
    public Annotation makeUpAnnotation(){
      return  new Annotation() {
            @Override
            public boolean equals(Object obj) {
                return false;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public String toString() {
                return null;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                Class<DisabledIf> disabledIfClass = DisabledIf.class;
                return disabledIfClass;
            }
        };
    }
    /**
     * just the test case of subclass do not write under the the base class. unless want run the base test.
     */
    @Test
    public void tryParent(){
        log.info("tryParent:{}",integer);
    }
}
@EnabledIf("false")
@Slf4j
class TestB extends Parent{
    @Test
    public void tryB(){
      log.info("tryB:{}",super.integer);
    }
}
@Slf4j
public class TestA extends Parent{
    @Test
    public void tryA(){
        log.info("tryA:{}",super.integer);
    }
}
