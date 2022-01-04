package sample.mybatis.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@KeySequence("user_account_sequence")
@Data
public class UserAccount {
    /**
     default:  firstly: KeySequence secondly:  Assign_id;
     */
    private String id;
    private BigDecimal money;
    @Version
    private Integer version;
}
