package com.zqq.blog_improve.service;

import com.zqq.blog_improve.common.pojo.dto.UserLoginDTO;
import com.zqq.blog_improve.common.pojo.vo.UserLoginVO;

public interface IUserService {
    /**
     * 处理用户登入逻辑
     * @param userLoginDTO 登入传入的 用户名，密码
     * @return 返回登入成功的 userId 和 token 令牌
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);
}
