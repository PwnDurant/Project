package com.zqq.user.service;


import com.zqq.user.api.pojo.UserInfoRegisterRequest;
import com.zqq.user.api.pojo.UserInfoRequest;
import com.zqq.user.api.pojo.UserInfoResponse;
import com.zqq.user.api.pojo.UserLoginResponse;

public interface UserService {
    UserLoginResponse login(UserInfoRequest user);

    UserInfoResponse getUserInfo(Integer userId);

    UserInfoResponse selectAuthorInfoByBlogId(Integer blogId);

    Integer register(UserInfoRegisterRequest userInfoRegisterRequest);
}
