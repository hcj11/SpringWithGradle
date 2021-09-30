package com.paic.context.web;

import com.paic.context.domain.Example;
import io.swagger.annotations.*;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
@ApiModel("授权实例")
@Data
class AuthorizeExample extends Example {
    @ApiModelProperty("认证信息")
    String principal;

}

@Api(value = "家")
@RequestMapping("home")
@RestController
public class HomeController {

    @ApiResponse(code = 200, message = "hello!!!")
    @ApiOperation(value = "家+操作")
    @GetMapping
    public String get(
            @ApiParam(name = "uri", value = "uri 参数") @RequestParam(value = "uri") String uri,
            @ApiParam(name = "path", value = "path 参数") @RequestParam(value = "path") String path) {
        return uri;
    }

    @PostMapping("post")
    @ApiOperation(value = "上传样例")
    public String post(@RequestBody
                       @ApiParam(value = "Created example object", required = true) Example example) {
        return example.getDescription();
    }

    /**
     * 认证
     */
    @ApiOperation(value = "认证操作",
            authorizations = @Authorization(value = "", scopes = @AuthorizationScope(scope = "", description = "")

    ))
    @ApiImplicitParams(
            @ApiImplicitParam(type = "body" , name = "/authorize", value = "")
    )
    @PostMapping("/authorize")
    public String authorize(@ApiParam(value = "Created example object", required = true) AuthorizeExample example,
    HttpServletRequest httpServletRequest) {
        //      自动填充3.  MetaObjectHandler.class
        // @TableField(fill = FieldFill.INSERT)  DATE gmt_create;
        //  @TableField(fill = FieldFill.INSERT_UPDATE) DATE gmt_create;


        return example.getPrincipal();
    }

}
