package org.mon.library_management_system.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.mon.library_management_system.model.UserInfo;

@Mapper
public interface UserMapper {
    @Select("select * from user_info where delete_flag=0 and user_name=#{name}")
    UserInfo selectUser(String name);
}
