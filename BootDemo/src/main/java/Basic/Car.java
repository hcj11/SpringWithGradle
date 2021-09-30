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
     *  firstly 先postBeanProcessor,最后一步的设置。
     */
    @PostConstruct
    public void inject(){

    }
    /**
     *  secondly 后 1. 参数确认 2. 属性设置。
     */
    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
