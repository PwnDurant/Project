package com.zqq.cookieshop.service;

import com.zqq.cookieshop.common.base.R;
import com.zqq.cookieshop.controller.user.dto.UserDTO;
import com.zqq.cookieshop.controller.user.vo.UserVO;

public interface IUserService {
    R<UserVO> login(UserDTO userDTO);

    R<UserVO> registerAdmin(UserDTO userDTO);

    R<UserVO> registerCommon(UserDTO userDTO);
}
