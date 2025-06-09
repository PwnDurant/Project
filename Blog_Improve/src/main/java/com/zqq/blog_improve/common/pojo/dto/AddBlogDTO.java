package com.zqq.blog_improve.common.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 添加博客说需要传递的参数
 */
@Data
public class AddBlogDTO {

    /**
     * 博客对应的作者 Id
     */
    @NotNull(message = "作者ID不能为空")
    private Long userId;

    /**
     * 博客对应的标题
     */
    @NotNull(message = "标题不能为空")
    private String title;

    /**
     * 博客对应的内容
     */
    @NotNull(message = "内容不能为空")
    private String content;

}
