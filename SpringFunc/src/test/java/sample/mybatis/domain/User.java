package sample.mybatis.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@KeySequence("user_sequence")
@Data
@TableName(value = "user")
public class User {
    @TableId(type = IdType.INPUT)
//    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    @TableField(fill = FieldFill.INSERT)
    private String name;
    private String phone;
    @Version
    private Integer version;

    /**
    you  can choose the id generate poicy
     @TableId(type = IdType.INPUT) + CustomIdGenerateMetaObjectHandler
     @TableId(type = IdType.ASSIGN_ID)
     @TableId(type = IdType.ASSIGN_UUID)
     */

    SFunction<User,String> sFunction = (User user)->{return "getName";};

}
