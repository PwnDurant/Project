package com.why.library.controller;


import com.why.library.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserService userService;

    @RequestMapping("/login")
    public boolean login(String name, String password, HttpSession session){
//        打印日志
        log.info("接收参数{}", name);
//        1,校验参数格式
//        2，从数据库中校验参数和密码是否正确
//        3，如果正确，存储session，返回true
        Boolean result=userService.checkUserAndPassword(name,password,session);
        log.info("用户登入结果，name：{},结果：{}",name,result);
        return result;
    }


}
