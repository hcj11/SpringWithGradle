package com.paic.context.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("授权实例")
@Data
public class Example {
    @ApiModelProperty("描述")
    private String description;

}
