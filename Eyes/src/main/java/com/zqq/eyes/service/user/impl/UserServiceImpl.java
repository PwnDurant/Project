package com.zqq.eyes.service.business.user.impl;

import com.zqq.eyes.domain.dto.UserDTO;
import com.zqq.eyes.service.business.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private AliSmsService aliSmsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCacheManager userCacheManager;

    @Value("${sms.code-expiration:5}")
    private Long phoneCodeExpiration;

    @Value("${sms.send-limit:10}")
    private Long sendLimit;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${sms.is-send:false}")
    private boolean isSend;

    @Value("${file.oss.downloadUrl}")
    private String downloadUrl;

    
    @Override
    public boolean sendCode(UserDTO userDTO) {
        
    }
}
