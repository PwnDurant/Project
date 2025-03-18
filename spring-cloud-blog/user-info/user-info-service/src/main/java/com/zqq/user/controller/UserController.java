package com.zqq.user.controller;

import com.zqq.common.pojo.Result;
import com.zqq.user.api.UserServiceApi;
import com.zqq.user.api.pojo.UserInfoRegisterRequest;
import com.zqq.user.api.pojo.UserInfoRequest;
import com.zqq.user.api.pojo.UserInfoResponse;
import com.zqq.user.api.pojo.UserLoginResponse;
import com.zqq.user.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController implements UserServiceApi {

    @Autowired
    private UserService userService;


    @Override
    public Result<UserLoginResponse> login(@Validated @RequestBody UserInfoRequest user){
        log.info("用户登录, userName: {}", user.getUserName());
        return Result.success(userService.login(user));
    }
    @Override
    public Result<UserInfoResponse> getUserInfo(@NotNull Integer userId){
        return Result.success(userService.getUserInfo(userId));
    }

    @Override
    public Result<UserInfoResponse> getAuthorInfo(@NotNull Integer blogId){
        return Result.success(userService.selectAuthorInfoByBlogId(blogId));
    }

    @Override
    public Result<Integer> register(@Validated @RequestBody UserInfoRegisterRequest userInfoRegisterRequest) {
        return Result.success(userService.register(userInfoRegisterRequest));
    }
}
