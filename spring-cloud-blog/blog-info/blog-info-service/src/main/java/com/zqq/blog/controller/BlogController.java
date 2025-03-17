package com.zqq.blog.controller;

import com.zqq.api.BlogServiceApi;
import com.zqq.api.pojo.AddBlogInfoRequest;
import com.zqq.api.pojo.BlogInfoResponse;
import com.zqq.api.pojo.UpBlogRequest;
import com.zqq.blog.service.BlogService;
import com.zqq.common.pojo.Result;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/blog")
@RestController
public class BlogController implements BlogServiceApi {
    @Autowired
    private BlogService blogService;


    @RequestMapping("/getList")
    public Result<List<BlogInfoResponse>> getList(){
        return Result.success(blogService.getList());
    }


    @RequestMapping("/getBlogDetail")
    public Result<BlogInfoResponse> getBlogDetail(@NotNull Integer blogId){
        log.info("getBlogDetail, blogId: {}", blogId);
        return Result.success(blogService.getBlogDetail(blogId));
    }


    @RequestMapping("/add")
    public Result<Boolean> addBlog(@Validated @RequestBody AddBlogInfoRequest addBlogInfoRequest){
        log.info("addBlog 接收参数: "+ addBlogInfoRequest);
        return Result.success(blogService.addBlog(addBlogInfoRequest));
    }


    @RequestMapping("/update")
    public Result<Boolean> updateBlog(@Valid @RequestBody UpBlogRequest upBlogRequest){
        log.info("updateBlog 接收参数: "+ upBlogRequest);
        return Result.success(blogService.update(upBlogRequest));

    }


    @RequestMapping("/delete")
    public Result<Boolean> deleteBlog(@NotNull Integer blogId){
        log.info("deleteBlog 接收参数: "+ blogId);
        return Result.success(blogService.delete(blogId));
    }
}
