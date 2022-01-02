package com;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
@Slf4j
@SpringBootApplication
public class StartUp {
    public void try1(){
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        SpelExpression spelExpression = spelExpressionParser.parseRaw(":");
        boolean b = spelExpression.compileExpression();

    }
    /** todo BeanExpressionResolver
     BeanExpressionResolver resolver = beanFactory.getBeanExpressionResolver();
     if (resolver == null) {
     resolver = new StandardBeanExpressionResolver();
     }
     BeanExpressionContext expressionContext = new BeanExpressionContext(beanFactory, null);
     Object result = resolver.evaluate(expression, expressionContext);
     return (result != null && (boolean) result);
     */
    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(StartUp.class, "--name=hcj","--like=like");
        ConfigurableEnvironment environment = run.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.stream().forEach(s->{
            log.info("s:{}",s);
        });



    }
}
