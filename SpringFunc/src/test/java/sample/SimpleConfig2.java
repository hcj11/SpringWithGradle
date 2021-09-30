package sample;

import context.CompontScan;
import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Data
@Configuration
@Import(value = {CompontScan.One.class})
public class SimpleConfig2 {

    @Autowired
    public CompontScan.One one;

    @Autowired
    public ObjectFactory<CompontScan.One> oneObjectFactory;

    @Autowired
    public ObjectProvider<CompontScan.One> oneObjectProvider;


}