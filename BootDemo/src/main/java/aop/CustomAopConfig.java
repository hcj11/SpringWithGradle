package aop;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

//@Import(AopConfig.class)
//@Order(10)
//@ImportAutoConfiguration(value={PropertyPlaceholderAutoConfiguration.class})
//@Configuration
//@PropertySource(name = "defaults", value = {"classpath:/application.properties"})
//@PropertySource(name = "auth", value = {"classpath:/auth.properties"},ignoreResourceNotFound = true)
//class BeforeConfiguration{}
class CustomFilterExcludeColAgent implements TypeFilter {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        boolean flag = metadataReader.getClassMetadata().getClassName()
                .equalsIgnoreCase("aop.ColAgent");
        if (flag) {
            return false;
        }
        return true;
    }
}
class  CustomFilterAll implements TypeFilter{

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        return true;
    }
}
class CustomFilterExcludeUserAgent implements TypeFilter {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        boolean flag = metadataReader.getClassMetadata().getClassName()
                .equalsIgnoreCase("aop.UserAgent");
        if (flag) {
            return false;
        }
        return true;
    }
}

@Order(100)
@Data
@EnableAspectJAutoProxy(proxyTargetClass = false)
@Configuration
public class CustomAopConfig {

    @ConditionalOnProperty(value = "Aopfilter.type" ,havingValue = "all")
    @Configuration
    @Data
    @ComponentScan(useDefaultFilters = false
            , basePackages = "aop", includeFilters = {
            @ComponentScan.Filter(type = FilterType.CUSTOM, classes = CustomFilterAll.class)
    }
    )
    class ComponetScanConfig {

    }

    @ConditionalOnProperty(value = "Aopfilter.type" ,havingValue = "ColAgent")
    @Configuration
    @Data
    @ComponentScan(useDefaultFilters = false
            , basePackages = "aop", includeFilters = {
            @ComponentScan.Filter(type = FilterType.CUSTOM, classes = CustomFilterExcludeUserAgent.class)
    }
    )
    class ComponetScanConfigForColAgent {

    }

    @ConditionalOnProperty(name = "Aopfilter.type",havingValue = "UserAgent")
    @Configuration
    @Data
    @ComponentScan(useDefaultFilters = false
            , basePackages = "aop", includeFilters = {
            @ComponentScan.Filter(type = FilterType.CUSTOM, classes = CustomFilterExcludeColAgent.class)
    }
    )
    class ComponetScanConfigForUserAgent {

    }
}
