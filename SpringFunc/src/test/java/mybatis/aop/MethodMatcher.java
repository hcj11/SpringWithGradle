package mybatis.aop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import sample.mybatis.mapper.MapperInterface;

import java.util.List;

public class MethodMatcher {
    @Test
    public void testMatchAnnotationOnClass2() throws NoSuchMethodException {
        String expressionForColAgent = "execution(void aop.UserService.*(..)) &&args(aop.OrgCodeDomain,java.util.HashMap)";
        AspectJExpressionPointcut ajexpForColAgent = new AspectJExpressionPointcut();
        ajexpForColAgent.setExpression(expressionForColAgent);
    }

    /**
     * 未确认 class扫面边界，
     */
    @Test
    public void testMatchAnnotationOnClassWithAnnoation() throws NoSuchMethodException {
//        String expression = "@annotation(sample.mybatis.annotation.EnableMysql)";
        // AspectJ scan class underneath classpath.
//        String expression = "@annotation(sample.mybatis.annotation.EnableMysql)||@annotation(sample.mybatis.annotation.EnablePg)";
        String expression = "execution(* sample.mybatis.mapper.MapperInterface.*(..)) && (@annotation(sample.mybatis.annotation.EnableMysql)||@annotation(sample.mybatis.annotation.EnablePg))";

        AspectJExpressionPointcut ajexp = new AspectJExpressionPointcut();
        ajexp.setExpression(expression);
        Assertions.assertTrue(ajexp.matches(MapperInterface.class.getMethod("findSomeThings"), MapperInterface.class));
        ;
    }

    @Test
    public void testMatchAnnotationOnClassWithReturnValue() throws NoSuchMethodException {
        String expression = "execution(* sample.mybatis.mapper.MapperInterface.*(..))";
        AspectJExpressionPointcut ajexp = new AspectJExpressionPointcut();
        ajexp.setExpression(expression);
        Assertions.assertTrue(ajexp.matches(MapperInterface.class.getMethod("findSomeThings"), MapperInterface.class));
        ;
        /**
         * void 算 *
         * 但是 void 就是void。
         */
        Assertions.assertTrue(ajexp.matches(MapperInterface.class.getMethod("insertList", List.class), MapperInterface.class));
        ;
    }

    @Test
    public void testMatchAnnotationOnClass1() throws NoSuchMethodException {
        String expression = "execution(void sample.mybatis.mapper.MapperInterface.*(..))";
        AspectJExpressionPointcut ajexp = new AspectJExpressionPointcut();
        ajexp.setExpression(expression);
        Assertions.assertFalse(ajexp.matches(MapperInterface.class.getMethod("findSomeThings"), MapperInterface.class));
        ;
        Assertions.assertTrue(ajexp.matches(MapperInterface.class.getMethod("insertList", List.class), MapperInterface.class));
        ;
    }
}