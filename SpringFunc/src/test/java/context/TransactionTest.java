package context;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.event.TransactionalEventListener;

public class TransactionTest {


    @EventListener(condition = "aa==1")
    @TransactionalEventListener
    @Test
    public void try1(){

    }
}
