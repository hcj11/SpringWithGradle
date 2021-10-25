package domain;

import lombok.Data;

import java.util.Date;

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
