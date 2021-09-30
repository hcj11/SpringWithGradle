package shiro.config;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class TemplateConfig implements InitializingBean {
    @Autowired
    freemarker.template.Configuration configuration;

    @Override
    public void afterPropertiesSet() throws Exception {
//        Path path = Paths.get("F:\\integration\\basic\\JVMDemo\\SpringDemo\\Shiro\\src\\main\\resources\\templates\\hello.ftl");
//        List<String> stringList = Files.readLines(path.toFile(),Charset.defaultCharset());
//        String collect = stringList.stream().collect(Collectors.joining(""));
//        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
//        stringTemplateLoader.putTemplate("template001",collect);
//        configuration.setTemplateLoader(stringTemplateLoader);

    }
    /**
     * stringTemplate
     */
    public void render() throws IOException, TemplateException {

        Path path = Paths.get("F:\\integration\\basic\\JVMDemo\\SpringDemo\\Shiro\\src\\main\\resources\\templates\\hello.json");
        HashMap<String, String> map = Maps.<String, String>newHashMap();
        map.put("username","ÍõÐ¡Ã÷");
        map.put("features","nice boy");
        map.put("img","www.baidu.com");
        Template template = configuration.getTemplate("template001");
        template.process(map, Files.newWriter(path.toFile(), Charset.defaultCharset()));


    }



}
