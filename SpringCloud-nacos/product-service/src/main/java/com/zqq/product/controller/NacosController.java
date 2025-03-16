package com.zqq.product.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//配置热更新
@RefreshScope
@RestController
public class NacosController {

    @Value("${nacos.config}")
    private String nacosConfig;

    @RequestMapping("/getConfig")
    public String getConfig(){
        return "从nacos获取配置项"+nacosConfig;
    }
}
