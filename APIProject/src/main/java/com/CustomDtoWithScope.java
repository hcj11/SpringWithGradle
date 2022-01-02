package com;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

import java.util.Date;
@Slf4j
@Scope(scopeName = "request")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomDtoWithScope {
    @JsonProperty("name")
    private String name;
    @JsonProperty("addDate")
    private Date addDate;

    public void doAction(){
        log.info("customDtoWithScope:{},{}",this.hashCode(),this);
    }
}