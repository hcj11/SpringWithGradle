package utils;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;

import java.util.stream.Stream;

public class Utils {
    public static void printSingleton(GenericApplicationContext context) {

        Stream.of(context.getDefaultListableBeanFactory().getSingletonNames()).forEach(System.out::println);
    }
    public static void print(ListableBeanFactory context) {

        Stream.of(context.getBeanDefinitionNames()).forEach(System.out::println);
    }
}
