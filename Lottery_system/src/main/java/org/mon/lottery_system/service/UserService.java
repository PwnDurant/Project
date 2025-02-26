package org.mon.lottery_system.service;


import org.mon.lottery_system.controller.param.UserRegisterParam;
import org.mon.lottery_system.service.dto.UserRegisterDTO;

public interface UserService {
    /**
     * 注册
     */
    UserRegisterDTO userRegisterDTO(UserRegisterParam userRegisterParam);

}
