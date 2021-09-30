package Quartz;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageDto implements Serializable {
    private static final long serialVersionUID = 42L;;
    private String jobId;
    private Integer no;
    private String name;
    private String cronExpression;
    private int status;
}
