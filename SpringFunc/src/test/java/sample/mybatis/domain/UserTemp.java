package sample.mybatis.domain;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserTemp {
    /**
     default:  firstly: use the grobal_config  is the    => input
     */
    private String id;
    private String remark;
    @Version
    private Integer version;
}
