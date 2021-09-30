package domain;

import lombok.Data;

import java.util.Date;

@Data
public class SerialCustomBean {
    private String data;
    private SubBean subBean;
    @Data
    public static class SubBean{
        private String subData;
        private String date;

    }

}
