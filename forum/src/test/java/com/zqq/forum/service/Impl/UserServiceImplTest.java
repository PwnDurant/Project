package com.zqq.forum.service.Impl;

import com.zqq.forum.model.User;
import com.zqq.forum.service.IUserService;
import com.zqq.forum.utils.MD5Util;
import com.zqq.forum.utils.UUIDUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Resource(name = "userServiceImpl")
    private IUserService userService;

//    这是一个事务的注解，在测试方法完成之后会自动进行回滚，在增删改上加上事务注解
    @Transactional
    @Test
    void createNormalUser() {
        User user=new User();
        user.setUsername("zhaoqianqian11");
        user.setNickname("chase");

        String password="123456";
        String salt = UUIDUtil.UUID_32();
        String ciphertext = MD5Util.md5Salt(password, salt);

        user.setPassword(ciphertext);
        user.setSalt(salt);

        userService.createNormalUser(user);

        System.out.println(user);
    }

    @Test
    void selectByUserName() {
        User user=userService.selectByUserName("zhaoqianqian");
        System.out.println(user);
    }

    @Test
    void login() {
        User user=userService.login("zhangsan","12345");
        System.out.println(user);
    }

    @Test
    void selectById() {
        System.out.println(userService.selectById(1L));
    }

    @Test
    @Transactional
    void addOneArticleCountById() {
        userService.addOneArticleCountById(1L);
    }

    @Test
    @Transactional
    void subOneArticleCountById() {
        userService.subOneArticleCountById(3L);
    }

    @Test
    void modifyInfo() {
        User user=new User();
        user.setId(2L);
        user.setUsername("zhangsan1");
        user.setNickname("chase1");
        user.setGender((byte)0);
        user.setEmail("qq@qq.com");
        user.setPassword("12345678910");
        user.setRemark("测试");
        userService.modifyInfo(user);
    }
}