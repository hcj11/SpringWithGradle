package Design;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class Config {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Config.class);
        DesignFactory bean = run.getBean(DesignFactory.class);
        Request request = bean.makeUpRequest(PriceType.A, "111");
        bean.getSendPrice(request);
        Request request2 = bean.makeUpRequest(PriceType.B, "222");
        bean.getSendPrice(request2);

        String aaa = "1";
        String initilia="2";
        aaa="3";


    }
}