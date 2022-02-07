package initial;

import com.Demo;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Log4jLoad {
    @BeforeEach
    public void setUp(){
        LogLog.setInternalDebugging(true);
    }
    @Test
    public void readFile(){
        String configFile = "";
        DOMConfigurator.configure(configFile);
    }
    @Test
    public void load(){
        Demo demo = new Demo();
        demo.get();
        demo.error();

    }

}
