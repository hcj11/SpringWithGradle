
package aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.lang.reflect.Method;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class CustomApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {


        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String type = environment.getProperty("Aopfilter.type");
//        String type =  System.getProperty("Aopfilter.type");
        log.info("selected the type is {}",type);

        TestPropertySourceUtils.
                addInlinedPropertiesToEnvironment(environment,"Aopfilter.type="+type);
        log.info("every method init properties.");
    }
}
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CustomAopConfig.class,
        initializers = {CustomApplicationContextInitializer.class})
public class AspectJProxyTest {

    @Autowired
    private ConfigurableEnvironment environment;
    @Autowired
    private Shopping shopping;
    @Autowired
    private ApplicationContext applicationContext;
    /**

     logging listener,

     */
    static {
        // and  logback.configurationFile
        /**
         类加载机制，先加载父类下面的信息，后加载子类的，最终实现初始化 so， 子类设置的数据，父类初始化的时候获取不到，
         */
//        System.setProperty("logback.configurationFile","logback-test.xml");


    }

    @Before
    public void setUp(){
        log.info("invoke before method");
    }
    @org.junit.Test
    public void tryLoggerLoad(){
        Log logger = LogFactory.getLog(getClass());
        logger.info("hello world");

    }

    /**
     and first the colAgent and then userAgent
     */
    @org.junit.Test
    public void tryMutilAopProyMethod(){

        shopping.showMap( new HashMap(16, 0.75f) {{
            put("i", "goit");
        }});

        /**
         how to ensure the 切面命中.
         如何将未命中的命中成功
         */
        /**
         aroundA  -> beforeA-> aroundB->beforeB->doMethod->after,

         assert order is

     1.         before invoke the method that name  is aroundForAddMap
     2.        before...
     3.      proxy{i=goit}
     4.    after...
         */

    }

    @org.junit.Test
    @Test
    public void try1(){
//        PrintUtils.print(applicationContext);
        shopping.addColsWithMap(null, new HashMap(16, 0.75f) {{
            put("i", "goit");
        }});
    }


}
