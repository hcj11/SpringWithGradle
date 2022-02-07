package context.domain;

import lombok.Data;

@Data
public class SerialCustomBean {
    private String data;
    private SubBean subBean = new SubBean();
    @Data
    public    class SubBean{
        private SerialCustomBean serialCustomBean;
        private String subData;
        private String date;

    }

}
