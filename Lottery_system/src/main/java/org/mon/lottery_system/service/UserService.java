package org.mon.lottery_system.service;


import org.mon.lottery_system.controller.param.UserLoginParam;
import org.mon.lottery_system.controller.param.UserPasswordLoginParam;
import org.mon.lottery_system.controller.param.UserRegisterParam;
import org.mon.lottery_system.service.dto.UserDTO;
import org.mon.lottery_system.service.dto.UserLoginDTO;
import org.mon.lottery_system.service.dto.UserRegisterDTO;
import org.mon.lottery_system.service.enums.UserIdentityEnum;

import java.util.List;

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

    /**
     * 根据身份查询人员列表
     * @param userIdentityEnum 如果为空，查询各个身份人员列表
     * @return
     */
    List<UserDTO> findUserInfo(UserIdentityEnum userIdentityEnum);

    List<UserDTO> findNormalInfo();



}
