package com.zqq.blog_improve.controller;

import com.zqq.blog_improve.common.base.R;
import com.zqq.blog_improve.common.pojo.dto.AddBlogDTO;
import com.zqq.blog_improve.common.pojo.dto.UpBlogDTO;
import com.zqq.blog_improve.common.pojo.vo.BlogInfoVO;
import com.zqq.blog_improve.service.IBlogService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource(name = "blogServiceImpl")
    private IBlogService blogService;

    /**
     * 获取博客列表
     * @return 返回博客信息列表
     */
    @GetMapping("/getList")
    public R<List<BlogInfoVO>> getList(){
        log.info("获取博客列表...");
        return blogService.getList();
    }

    /**
     * 获取博客详情
     */
    @GetMapping("/getBlogDetail")
    public R<BlogInfoVO> getBlogDetail(@NotNull Long blogId){
        log.info("获取博客列表详情，blogId:{}",blogId);
        return blogService.getBlogDetail(blogId);
    }

    /**
     * 添加博客
     */
    @PostMapping("/add")
    public R<Boolean> addBlog(@Validated @RequestBody AddBlogDTO addBlogDTO){
        log.info("添加博客,标题为:{}",addBlogDTO.getTitle());
        return blogService.addBlog(addBlogDTO);
    }

    /**
     * 更新博客
     */
    @PostMapping("/update")
    public R<Boolean> updateBlog(@RequestBody @Validated UpBlogDTO upBlogDTO){
        log.info("更新博客,标题为:{}",upBlogDTO.getId());
        return blogService.updateBlog(upBlogDTO);
    }

    /**
     * 删除博客
     */
    @PostMapping("/delete")
    public R<Boolean> deleteBlog(@NotNull Long blogId){
        log.info("删除博客,标题为:{}",blogId);
        return blogService.deleteBlog(blogId);
    }







}
