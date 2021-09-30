package sample;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

public interface FooService {
    public String foo(int id);
    /**
     *
     */
    @Scope(proxyMode = ScopedProxyMode.DEFAULT )
    public class SubFooService implements FooService{

        @Override
        public String foo(int id) {
            return "bar";
        }
    }
}


