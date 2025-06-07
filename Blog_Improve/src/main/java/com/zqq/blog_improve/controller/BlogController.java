package com.zqq.blog_improve.controller;

import com.zqq.blog_improve.service.IBlogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource(name = "blogServiceImpl")
    private IBlogService blogService;



}
