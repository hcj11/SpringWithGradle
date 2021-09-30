package sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class CustomTypeFilter implements TypeFilter {

    /**
     * 代理去构建
     */
    @Primary
    @Qualifier
    public static class KDependentKCustomA {

    }

    public static class KCustomA {

        @Autowired
        @Qualifier
        private KDependentKCustomA kDependentKCustomA;

        public KDependentKCustomA getDependent() {
            return this.kDependentKCustomA;
        }
    }
    /**
     * scan  beanDefination -> bean
     */
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        return metadataReader.getClassMetadata().getClassName().contains("KCustom");
    }
}
