package com.zqq.cookieshop.controller.user;

import com.zqq.cookieshop.common.base.R;
import com.zqq.cookieshop.controller.user.dto.UserDTO;
import com.zqq.cookieshop.controller.user.vo.UserVO;
import com.zqq.cookieshop.service.IUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource(name = "userServiceImpl")
    private IUserService userService;

    @PostMapping("/login")
    public R<UserVO> login(@RequestBody @Validated UserDTO userDTO){
        log.info("用户登陆:userVO:{}",userDTO);
        return userService.login(userDTO);
    }

    @PostMapping("/registerAdmin")
    public R<UserVO> registerAdmin(@RequestBody @Validated UserDTO userDTO){
        log.info("管理员用户注册:userVO:{}",userDTO);
        return userService.registerAdmin(userDTO);
    }

    @PostMapping("/registerCommon")
    public R<UserVO> registerCommon(@RequestBody @Validated UserDTO userDTO){
        log.info("普通用户注册:userVO:{}",userDTO);
        return userService.registerCommon(userDTO);
    }

}
