package com.zqq.blog_improve.service;

import com.zqq.blog_improve.common.base.R;
import com.zqq.blog_improve.common.pojo.dto.AddBlogDTO;
import com.zqq.blog_improve.common.pojo.dto.UpBlogDTO;
import com.zqq.blog_improve.common.pojo.vo.BlogInfoVO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface IBlogService {

    /**
     * 获取博客列表
     * @return 返回博客信息列表
     */
    R<List<BlogInfoVO>> getList();

    /**
     * 获取博客详情
     * @param blogId 博客Id
     * @return 博客详情
     */
    R<BlogInfoVO> getBlogDetail(@NotNull Long blogId);

    /**
     * 添加博客
     * @param addBlogDTO 添加博客传递的参数
     * @return 是否添加成功
     */
    R<Boolean> addBlog(AddBlogDTO addBlogDTO);

    /**
     * 更新博客
     * @param upBlogDTO 更新的数据
     * @return 是否更新成功
     */
    R<Boolean> updateBlog(UpBlogDTO upBlogDTO);

    /**
     * 删除博客
     * @param blogId 需要删除的博客 ID
     * @return 是否删除成功
     */
    R<Boolean> deleteBlog(@NotNull Long blogId);
}
