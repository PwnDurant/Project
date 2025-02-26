package org.mon.lottery_system.service;


import org.mon.lottery_system.controller.param.UserLoginParam;
import org.mon.lottery_system.controller.param.UserPasswordLoginParam;
import org.mon.lottery_system.controller.param.UserRegisterParam;
import org.mon.lottery_system.service.dto.UserLoginDTO;
import org.mon.lottery_system.service.dto.UserRegisterDTO;

public interface UserService {
    /**
     * 注册
     */
    UserRegisterDTO userRegisterDTO(UserRegisterParam userRegisterParam);


    /**
     * 用户登入
     * @param
     * @return
     */
    UserLoginDTO login(UserLoginParam userLoginParam);
}
