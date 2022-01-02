package com;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

import java.util.Date;



@Data
@Slf4j
public class ScopeTest {
    private CustomDtoWithScope customDtoWithScope;

    public CustomDtoWithScope customDtoWithScope(String name,Date addDate){
       return new CustomDtoWithScope(name,addDate);
    }
    /**
     *
     */
    public void try1(CustomDtoWithScope customDtoWithScope){
        log.info("customDtoWithScope:{},{}",customDtoWithScope.hashCode(),customDtoWithScope);
    }
}