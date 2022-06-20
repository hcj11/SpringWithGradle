package Basic;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;

@Data
public class Car implements InitializingBean {
    private String name;
    private String wheel;
    private String tags;
    /**
     */
    @PostConstruct
    public void inject(){

    }
    /**
     */
    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
