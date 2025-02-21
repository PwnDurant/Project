package org.mon.blog_system.service;

import jakarta.validation.constraints.NotNull;
import org.mon.blog_system.common.pojo.Request.UserLoginParam;
import org.mon.blog_system.common.pojo.response.UserInfoResponse;
import org.mon.blog_system.common.pojo.response.UserLoginResponse;

public interface UserService {
    UserLoginResponse login(UserLoginParam userLoginParam);

    UserInfoResponse getUserInfoById(@NotNull Integer userId);

    UserInfoResponse getAuthorInfoById(@NotNull Integer blogId);
}
