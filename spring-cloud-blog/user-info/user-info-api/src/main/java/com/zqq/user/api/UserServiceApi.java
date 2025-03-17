package com.zqq.user.api;


import com.zqq.common.pojo.Result;
import com.zqq.user.api.pojo.UserInfoRegisterRequest;
import com.zqq.user.api.pojo.UserInfoRequest;
import com.zqq.user.api.pojo.UserInfoResponse;
import com.zqq.user.api.pojo.UserLoginResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-service",path = "/user")
public interface UserServiceApi {

    @RequestMapping("/login")
    Result<UserLoginResponse> login(@RequestBody UserInfoRequest user);

    @RequestMapping("/getUserInfo")
    Result<UserInfoResponse> getUserInfo(@RequestParam("userId") Integer userId);

    @RequestMapping("/getAuthorInfo")
    Result<UserInfoResponse> getAuthorInfo(@RequestParam("blogId") Integer blogId);

    @RequestMapping("/register")
    Result<Integer> register(@RequestBody UserInfoRegisterRequest userInfoRegisterRequest);
}
