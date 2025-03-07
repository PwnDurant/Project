package org.mon.gobang.model;


import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

//    往数据库中插入一个用户，用于注册
    void insert(User user);

//    根据用户名，来查询用户的详细信息，用于登入功能
    User selectByName(String username);

    void userWin(int userId);

    void userLose(int userId);
}
