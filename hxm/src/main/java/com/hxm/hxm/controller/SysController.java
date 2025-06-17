package com.hxm.hxm.controller;

import com.hxm.hxm.common.base.R;
import com.hxm.hxm.domain.SysInfo;
import com.hxm.hxm.service.SysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys")
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
