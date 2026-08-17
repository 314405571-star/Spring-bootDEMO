package com.demo.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.model.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.List;
@Mapper
public interface AccountMapper extends BaseMapper<Account> {
    @Update("<script>" +
            "UPDATE account" +
            "<set>" +
            "<if test='name != null'>name = #{name},</if>" +
            "<if test='balance != null'>balance = #{balance},</if>" +
            "<if test='type != null'>type = #{type},</if>" +
            "</set>" +
            " WHERE id = #{id}" +
            "</script>")
    int updateAccount(Account account);
    int deleteAccountById(@Param("id") Long id);
    int deleteAccountByIds(@Param("ids") List<Long> ids);
}
