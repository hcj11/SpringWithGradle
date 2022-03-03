package boot;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class OtherDemo {
    @BeforeAll
    public static void setUp(){
        log.info("i am here!!!, to setUp");
    }
    @Test
    public void try1(){

    }

}
