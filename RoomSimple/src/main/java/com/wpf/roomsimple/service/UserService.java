package com.wpf.roomsimple.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wpf.roomsimple.domain.UserInfo;
import com.wpf.roomsimple.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public Boolean login(UserInfo userInfo) {
        UserInfo userInfo1 = userMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserName, userInfo.getUserName()));
        if(userInfo1==null) return false;
        return userInfo1.getPassword().equals(userInfo.getPassword());
    }

    public Boolean register(UserInfo userInfo){
        UserInfo userInfo1 = userMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserName, userInfo.getUserName()));
        if(userInfo1!=null) return false;
        int insert = userMapper.insert(userInfo);
        return insert==1;
    }


}
