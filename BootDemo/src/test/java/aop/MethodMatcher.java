package aop;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MethodMatcher {

    @org.junit.Test
    public void testMatchAnnotationOnClass() throws NoSuchMethodException {
        String expression = "execution(void aop.UserService.*(..))";
        String expressionForColAgent = "execution(void aop.UserService.*(..)) &&args(aop.OrgCodeDomain,java.util.HashMap)";

        AspectJExpressionPointcut ajexpForColAgent = new AspectJExpressionPointcut();
        ajexpForColAgent.setExpression(expressionForColAgent);

        AspectJExpressionPointcut ajexp = new AspectJExpressionPointcut();
        ajexp.setExpression(expression);
        Method showMap = UserService.class.getMethod("showMap", Map.class);
        assertThat(ajexp.matches(showMap,UserService.class)).isTrue();
        assertThat(ajexp.matches(UserService.class.getMethod("addColsWithMap",OrgCodeDomain.class, HashMap.class),UserService.class)).isTrue();
        assertThat(ajexpForColAgent.matches(UserService.class.getMethod("addColsWithMap",OrgCodeDomain.class, HashMap.class),UserService.class)).isTrue();


    }
}
