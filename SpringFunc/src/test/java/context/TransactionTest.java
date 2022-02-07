package context;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.AbstractApplicationEventMulticaster;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.event.TransactionalEventListener;
@Configuration
public class TransactionTest {

    @Autowired
    private AbstractApplicationEventMulticaster multicaster;

    @EventListener(condition = "aa==1")
    @TransactionalEventListener
    @Test
    public void try1(){
        multicaster.multicastEvent(null);
    }
}
