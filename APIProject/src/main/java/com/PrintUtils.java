package com;

import org.springframework.beans.factory.ListableBeanFactory;

import java.util.stream.Stream;

public class PrintUtils {
    public static void print(ListableBeanFactory context) {

        Stream.of(context.getBeanDefinitionNames()).forEach(System.out::println);
    }
}