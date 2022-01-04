package sample.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import mybatis.SqlProvider;
import org.apache.ibatis.annotations.*;
import sample.mybatis.annotation.ROLE1;
import sample.mybatis.annotation.ROLE2;
import sample.mybatis.annotation.SqlCheck;
import sample.mybatis.domain.User;
import sample.mybatis.domain.UserAccount;
import sample.mybatis.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserAccountInterface extends BaseMapper<UserAccount> {


}
