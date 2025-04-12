package com.zqq.eyes.service.business.user;

import com.zqq.eyes.domain.dto.UserDTO;

public interface IUserService {
    boolean sendCode(UserDTO userDTO);
}
