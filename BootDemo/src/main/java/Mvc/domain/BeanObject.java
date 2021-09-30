package Mvc.domain;

import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@ToString
@Setter
@Accessors(chain = true)
@XmlRootElement(name = "root" )
public class BeanObject implements Serializable {

    private static final long serialVersionUID = -5022806638005972375L;

    @XmlID
    public String getCustomerID(){return UUID.randomUUID().toString();};

    @XmlElement
    private Bean bean;
    @XmlAttribute
    private List<String> list;


    @Accessors(chain = true)
    @Setter
    public static class Bean{
        @XmlAttribute
        private String name;

        @XmlElement
        private List<Properties> property;

    }
    @Accessors(chain = true)
    @Setter
    public static class Properties{
        @XmlAttribute
        private String name;
        @XmlValue
        private String val;
    }
}