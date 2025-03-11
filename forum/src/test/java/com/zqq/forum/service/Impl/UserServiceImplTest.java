package com.zqq.forum.service.Impl;

import com.zqq.forum.model.User;
import com.zqq.forum.service.IUserService;
import com.zqq.forum.utils.MD5Util;
import com.zqq.forum.utils.UUIDUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Resource(name = "userServiceImpl")
    private IUserService userService;

    @Test
    void createNormalUser() {
        User user=new User();
        user.setUsername("zhaoqianqian");
        user.setNickname("chase");

        String password="123456";
        String salt = UUIDUtil.UUID_32();
        String ciphertext = MD5Util.md5Salt(password, salt);

        user.setPassword(ciphertext);
        user.setSalt(salt);

        userService.createNormalUser(user);

        System.out.println(user);
    }
}