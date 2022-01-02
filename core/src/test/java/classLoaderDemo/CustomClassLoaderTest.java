package classLoaderDemo;

import classLoader.CustomClassLoader;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

@JSONType
@EqualsAndHashCode(callSuper=false)
public class CustomClassLoaderTest  extends ClassLoader{
    public static void demo2()  throws ClassNotFoundException  {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        ClassLoader parent = systemClassLoader.getParent();
        System.out.println(parent);


        Class<?> aClass = systemClassLoader.loadClass(CustomClassLoader.class.getName());
        System.out.println(aClass);
        String annotation = aClass.getName();
        Annotation[] annotations = aClass.getAnnotations();


        System.out.println(annotation);
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(CustomClassLoader.class);
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(CustomClassLoader.class);
        beanDefinitionBuilder.addPropertyValue("", "");
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
    }
    public static void demo1() throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        URL url = new URL("file://d:/test.jar");
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url}, contextClassLoader);
        Class<?> aClass = urlClassLoader.loadClass(CustomClassLoader.class.getName());
        Object o = aClass.newInstance();

    }
    public static void demo3() throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        URL url = new URL("file://d:/test.jar");
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});

    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }
}
