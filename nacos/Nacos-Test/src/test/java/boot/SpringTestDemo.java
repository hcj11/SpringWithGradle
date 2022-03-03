package boot;

import lombok.Data;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DefaultTestContextBootstrapper;
import utils.Utils;

import static org.mockito.Mockito.when;

@Data
class A {
}
@Data
class B{}

@RunWith(SpringJUnit4ClassRunner.class)
public class SpringTestDemo {
    @Mock
    private A a;
    @InjectMocks
    private B b ;

    @Autowired
    GenericApplicationContext genericApplicationContext;

    @Before
    public void setUp() {
        when(a.toString()).thenReturn("hello");
//        MockitoAnnotations.initMocks(SpringTestDemo.class);
    }

    @Test
    public void try2() {
        Class<DefaultTestContextBootstrapper> bootstrapperClass = DefaultTestContextBootstrapper.class;
        /**
         * DefaultTestContextBootstrapper
         *
         */
    }

    @Test
    public void try1() {
        Assert.assertTrue(a != null);
        Assert.assertEquals(a.toString(), "hello");
        Assert.assertNotEquals(b.toString(), "Bhello");
        Assert.assertTrue(a instanceof A);
        Assert.assertTrue(b instanceof  B);

        Utils.print(genericApplicationContext);
        Assert.assertFalse(genericApplicationContext.containsBean("a"));;

    }
}
