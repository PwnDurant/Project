package com.zqq.forum.controller;

import com.zqq.forum.common.AppResult;
import com.zqq.forum.common.ResultCode;
import com.zqq.forum.model.User;
import com.zqq.forum.service.IUserService;
import com.zqq.forum.utils.MD5Util;
import com.zqq.forum.utils.UUIDUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource(name = "userServiceImpl")
    private IUserService iUserService;

    /**
     * 用户注册
     * @return
     */
    @PostMapping("/register")
    public AppResult register(@RequestParam("username") @Nonnull String username,
                              @RequestParam("nickname") @Nonnull String nickname,
                              @RequestParam("password") @Nonnull String password,
                              @RequestParam("passwordRepeat") @Nonnull String passwordRepeat){
//        校验密码是否相同
        if(!password.equals(passwordRepeat)){
            log.warn(ResultCode.FAILED_TWO_PWD_NOT_SAME.toString());
            return AppResult.failed(ResultCode.FAILED_TWO_PWD_NOT_SAME);
        }
//        准备数据
        User user=new User();
        user.setUsername(username);
        user.setNickname(nickname);
//        处理密码
        String salt= UUIDUtil.UUID_32();
        String encryptPassword = MD5Util.md5Salt(password, salt);

        user.setPassword(encryptPassword);
        user.setSalt(salt);

        iUserService.createNormalUser(user);

        return AppResult.success();
    }

}
