package demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Others {
    static class P{

    }
    static class S extends  P {

    }
    /**
     * 	if(factory.getConfigClass().isAssignableFrom(FilterDefinition.class)){
     *
     *                        }
     */
    @Test
    public void test(){
        P p = new P();
        S s = new S();
        // s->p
        boolean assignableFrom = p.getClass().isAssignableFrom(S.class);

        P p1 = (P) s;
        assertThrows(ClassCastException.class, ()->{
            S p2 = (S) p;
        });

        Assertions.assertTrue(assignableFrom);

    }
}
