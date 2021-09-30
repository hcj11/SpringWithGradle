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
     *  firstly ��postBeanProcessor,���һ�������á�
     */
    @PostConstruct
    public void inject(){

    }
    /**
     *  secondly �� 1. ����ȷ�� 2. �������á�
     */
    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
