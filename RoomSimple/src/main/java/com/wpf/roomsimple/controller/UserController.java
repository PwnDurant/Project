package com.wpf.roomsimple.controller;

import com.wpf.roomsimple.common.base.R;
import com.wpf.roomsimple.domain.UserInfo;
import com.wpf.roomsimple.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public R<Boolean> login(@RequestBody UserInfo userInfo){
        return R.ok(userService.login(userInfo));
    }

    @PostMapping("/register")
    public R<Boolean> register(@RequestBody UserInfo userInfo){
        return R.ok(userService.register(userInfo));
    }
}
