package com.thwh.shopmall.controller;


import com.thwh.shopmall.common.base.R;
import com.thwh.shopmall.domain.SysInfo;
import com.thwh.shopmall.service.SysService;
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
