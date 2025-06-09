package com.zqq.blog_improve.common.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpBlogDTO {

    /**
     * 更新的博客 ID
     */
    @NotNull(message = "博客Id不能为空")
    private Long id;

    /**
     * 更新的博客标题
     */
    @NotNull(message = "标题不能为空")
    private String title;

    /**
     * 更新的博客内容
     */
    @NotNull(message = "内容不能为空")
    private String content;

}
