package context.beanFactory;

import context.CompontScan;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class BeanFactory {
    @Test
    public void demo1() throws IOException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/demo.xml");

        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory((Resource) new InputStreamResource(resourceAsStream));
        Fruits fruits = xmlBeanFactory.getBean("fruits", Fruits.class);
        log.info("{}", fruits);

    }

    @Test
    public void reader() {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/demo.xml");
        GenericXmlApplicationContext genericXmlApplicationContext = new GenericXmlApplicationContext();
        genericXmlApplicationContext.setValidating(false);
        genericXmlApplicationContext.load(new InputStreamResource(resourceAsStream));
        genericXmlApplicationContext.refresh();
        Fruits fruits = genericXmlApplicationContext.getBean("fruits", Fruits.class);
        log.info("{}", fruits);
        CompontScan.print(genericXmlApplicationContext);
        CompontScan.printSingleton(genericXmlApplicationContext);

    }

    private class Fruits {
    }
}
