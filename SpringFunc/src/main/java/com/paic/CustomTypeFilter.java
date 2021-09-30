package com.paic;

import cn.hutool.core.io.FileUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
public class CustomTypeFilter implements TypeFilter  {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        if(classMetadata.getClassName().equalsIgnoreCase(Config.class.getName())){
            return true;
        };
        return false;
    }
    public static void main(String[] args) throws IOException {
        final String var1 = System.getProperty("java.class.path");
        log.info("{}",var1);

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        log.info("classloader========={}",cl.toString());
        org.springframework.core.io.ClassPathResource classPathResource =   new org.springframework.core.io.ClassPathResource("application.properties");
        BufferedReader reader = FileUtil.getReader(classPathResource.getFile(), Charset.defaultCharset());
        Properties properties = new Properties();
        properties.load(reader);
        properties.list(System.out);

    }


}
