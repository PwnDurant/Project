package org.mon.library_management_system.service;


import jakarta.servlet.http.HttpSession;
import org.mon.library_management_system.constant.Constants;
import org.mon.library_management_system.mapper.UserMapper;
import org.mon.library_management_system.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {

    @Autowired
    public UserMapper userMapper;

    public Boolean checkUserAndPassword(String name, String password, HttpSession session) {
        if(!StringUtils.hasLength(name)||!StringUtils.hasLength(password)){
            return false;
        }
        UserInfo userInfo=userMapper.selectUser(name);
        if(userInfo==null){
            return false;
        }
        if(password.equals(userInfo.getPassword())){
//            1，账号密码是正确的
//            2，session的内容，取决于后面需要从session中获取什么
            session.setAttribute(Constants.SESSION_USER_KEY,name);
            return true;
            }
        return false;
    }
}
