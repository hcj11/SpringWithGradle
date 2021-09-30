package sample;

import lombok.Getter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

public class CustomImportWare implements ImportAware {


    private AnnotationMetadata annotationMetadata;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.annotationMetadata = importMetadata;
    }

    @Bean
    public MetaHolder metaHolder() {
        return new MetaHolder(annotationMetadata);
    }

    @Getter
    public static class MetaHolder {
        private AnnotationMetadata annotationMetadata;
        public MetaHolder() {
        }
        public MetaHolder(AnnotationMetadata annotationMetadata) {
            this.annotationMetadata = annotationMetadata;
        }

    }
}
