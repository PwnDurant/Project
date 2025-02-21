package org.mon.blog_system.controller;


import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.mon.blog_system.common.pojo.Request.UserLoginParam;
import org.mon.blog_system.common.pojo.response.UserInfoResponse;
import org.mon.blog_system.common.pojo.response.UserLoginResponse;
import org.mon.blog_system.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

//    用户输入账号密码，发送给服务端
//    服务端生成token，返回给用户
//    用户携带token，发起请求（比如博客列表）
//    服务端校验token是否正确，正确的话，进行响应，如果不正确，让用户重新登入

    @Resource(name = "userServiceImpl")
    private UserService userService;



    @PostMapping("/login")
//    校验实体类
    public UserLoginResponse login(@Validated @RequestBody UserLoginParam userLoginParam){
        log.info("用户登入,userName:{}",userLoginParam.getUserName());
        return userService.login(userLoginParam);
    }

    @GetMapping("/getUserInfo")
    public UserInfoResponse getUserInfo(@NotNull Integer userId){
        log.info("获取用户信息,userId:{}",userId);
        return userService.getUserInfoById(userId);
    }

    @GetMapping("/getAuthorInfo")
    public UserInfoResponse getAuthorInfo(@NotNull Integer blogId){
        log.info("获取坐着信息,blogId:{}",blogId);
        return userService.getAuthorInfoById(blogId);
    }


}
