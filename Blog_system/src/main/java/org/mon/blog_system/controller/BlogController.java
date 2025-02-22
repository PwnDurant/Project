package org.mon.blog_system.controller;


import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.mon.blog_system.common.pojo.Request.AddBlogParam;
import org.mon.blog_system.common.pojo.Request.UpBlogParam;
import org.mon.blog_system.common.pojo.response.BlogInfoResponse;
import org.mon.blog_system.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/blog")
public class BlogController {


//    运用了通过接口来实例化一个对象的思想，一个接口可以有多个不同的实现类，方便来回进行切换实现类
    @Resource(name = "blogServiceImpl")
    private BlogService blogService;

//    @Autowired  也可以通过这个注解进行注入，但是名字要写成Bean的名称
//    private BlogService blogServiceImpl;

    @GetMapping("/getList")
    public List<BlogInfoResponse> getList(){
        log.info("获取博客列表......");
        return blogService.getList();
    }


    @GetMapping("/getBlogDetail")

    public BlogInfoResponse getBlogDetail(@NotNull Integer blogId){
        log.info("获取博客详情,blogId:{}",blogId);
        return blogService.getBlogDetail(blogId);
    }

    @PostMapping("/add")
//    前端传的是JSON
    public Boolean addBlog(@Validated @RequestBody AddBlogParam addBlogParam){
        log.info("添加博客，标题:{}",addBlogParam.getTitle());
        return blogService.addBlog(addBlogParam);
    }

    @PostMapping("/update")
    public Boolean updateBlog(@RequestBody @Validated UpBlogParam upBlogParam){
        log.info("更新博客，博客Id为:{}",upBlogParam.getId());
        return blogService.updateBlog(upBlogParam);
    }

    @PostMapping("/delete")
    public Boolean deleteBlog(@NotNull Integer blogId){
        log.info("删除博客，博客Id为:{}",blogId);
        return blogService.deleteBlog(blogId);
    }
}
