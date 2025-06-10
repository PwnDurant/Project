package com.zyp.note.controller;

import com.zyp.note.common.base.R;
import com.zyp.note.pojo.domain.UserInfo;
import com.zyp.note.service.IUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource(name = "userServiceImpl")
    private IUserService userService;

    @PostMapping("/login")
    public R<Boolean> login(@RequestBody  UserInfo userInfo){
        return userService.login(userInfo);
    }

    @PostMapping("/register")
    public R<Boolean> register(@RequestBody  UserInfo userInfo){
        return R.ok(userService.register(userInfo));
    }

}
