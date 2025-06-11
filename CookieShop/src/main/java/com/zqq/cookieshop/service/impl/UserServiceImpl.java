package com.zqq.cookieshop.service.impl;

import com.zqq.cookieshop.mapper.UserMapper;
import com.zqq.cookieshop.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserMapper userMapper;

}
