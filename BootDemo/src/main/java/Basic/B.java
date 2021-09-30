package Basic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
class A {
    @Autowired
    private B b;
}
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class B {
    @Autowired
    private A a;

    @Async
    public void async(){
      log.info("async ... ");
    }
}
