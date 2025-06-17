package com.ygt.park.controller;

import com.ygt.park.common.base.R;
import com.ygt.park.domain.SysInfo;
import com.ygt.park.service.SysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Sys")
public class SysController {

    @Autowired
    private SysService sysService;

    @PostMapping("/login")
    public R<Boolean> login(@RequestBody SysInfo sysInfo){
        return sysService.login(sysInfo);
    }

    @PostMapping("/register")
    public R<Boolean> register(@RequestBody SysInfo sysInfo){
        return R.ok(sysService.register(sysInfo));
    }

}
