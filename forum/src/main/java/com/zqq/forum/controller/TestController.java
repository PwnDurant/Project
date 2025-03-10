package com.zqq.forum.controller;

import com.zqq.forum.common.AppResult;
import com.zqq.forum.exception.ApplicationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/exception")
    public AppResult testException() throws Exception{
        throw new Exception("这是一个Exception...");
    }

    @GetMapping("/appException")
    public AppResult testApplicationException() throws ApplicationException {
        throw new ApplicationException("这是一个AppException...");
    }


}
