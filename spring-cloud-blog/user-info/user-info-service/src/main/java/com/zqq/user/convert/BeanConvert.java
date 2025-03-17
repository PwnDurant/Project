package com.zqq.user.convert;

import com.zqq.common.utils.SecurityUtil;
import com.zqq.user.api.pojo.UserInfoRegisterRequest;
import com.zqq.user.dataobject.UserInfo;

public class BeanConvert {

    public static UserInfo convertUserInfoByEncrypt(UserInfoRegisterRequest userInfoRegisterRequest){
        UserInfo userInfo=new UserInfo();
        userInfo.setUserName(userInfoRegisterRequest.getUserName());
        userInfo.setPassword(SecurityUtil.encrypt(userInfoRegisterRequest.getPassword()));
        userInfo.setGithubUrl(userInfoRegisterRequest.getGithubUrl());
        userInfo.setEmail(userInfoRegisterRequest.getEmail());
        return userInfo;
    }

}
