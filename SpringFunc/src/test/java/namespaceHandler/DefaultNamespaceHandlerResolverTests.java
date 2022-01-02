package namespaceHandler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.xml.DefaultNamespaceHandlerResolver;
import org.springframework.beans.factory.xml.UtilNamespaceHandler;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultNamespaceHandlerResolverTests {

        @Test
        public void testResolvedMappedHandler() {
            new ConcurrentHashMap<>();
            new HashMap<>();

            DefaultNamespaceHandlerResolver resolver = new DefaultNamespaceHandlerResolver(getClass().getClassLoader());
            org.springframework.beans.factory.xml.NamespaceHandler handler = resolver.resolve("http://www.springframework.org/schema/util");
            assertThat(handler).as("Handler should not be null.").isNotNull();
            assertThat(handler.getClass()).as("Incorrect handler loaded").isEqualTo(UtilNamespaceHandler.class);
        }
}

