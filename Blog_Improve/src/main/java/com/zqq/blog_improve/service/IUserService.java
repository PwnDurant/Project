package com.zqq.blog_improve.service;

import com.zqq.blog_improve.common.base.R;
import com.zqq.blog_improve.common.pojo.dto.UserLoginDTO;
import com.zqq.blog_improve.common.pojo.vo.UserInfoVO;
import com.zqq.blog_improve.common.pojo.vo.UserLoginVO;
import jakarta.validation.constraints.NotNull;

public interface IUserService {
    /**
     * 处理用户登入逻辑
     * @param userLoginDTO 登入传入的 用户名，密码
     * @return 返回登入成功的 userId 和 token 令牌
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);

    /**
     * 获得用户信息
     * @param userId 用户Id
     * @return 返回用户信息
     */
    R<UserInfoVO> getUserInfoById(@NotNull Integer userId);

    /**
     * 获取作者信息
     * @param blogId 博客 id
     * @return 返回对应博客id 的用户信息
     */
    R<UserInfoVO> getAuthorInfoById(@NotNull Integer blogId);

    /**
     * 处理用户注册请求
     * @param userLoginDTO 注册需要携带的数据：账号名和密码
     * @return 是否注册成功
     */
    Boolean register(UserLoginDTO userLoginDTO);
}
