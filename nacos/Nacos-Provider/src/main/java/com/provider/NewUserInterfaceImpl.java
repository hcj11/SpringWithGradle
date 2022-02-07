package com.provider;

import com.api.UserInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@org.apache.dubbo.config.annotation.Service(interfaceClass = UserInterface.class,version = "2.5.7")
@EqualsAndHashCode(callSuper = false)
@Data
public class NewUserInterfaceImpl implements UserInterface {

    @Override
    public String getName() {
        return "hcjForNewDubbo";
    }
}