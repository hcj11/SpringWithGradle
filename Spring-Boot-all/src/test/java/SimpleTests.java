import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import simple.Simple;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimpleTests {

    @Test
    public void simple(){
        Simple mock = mock(Simple.class);
        when(mock.getName()).thenReturn("hcj1");
        Assertions.assertEquals(mock.getName(),"hcj1");;

    }
}
