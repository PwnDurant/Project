package com.why.library.mapper;


import com.why.library.dao.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user_info where delete_flag=0 and user_name=#{name}")
    UserInfo selectUser(String name);
}
