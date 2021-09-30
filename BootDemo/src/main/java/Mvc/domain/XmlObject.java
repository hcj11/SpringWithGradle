package Mvc.domain;

import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



@ToString
@Setter
@Accessors(chain = true)
@XmlRootElement(name = "root" )
 public class XmlObject{
    @XmlElement(name = "subname",required = true)
    private String subname;
    @XmlElement
    private String requiredEle;

}