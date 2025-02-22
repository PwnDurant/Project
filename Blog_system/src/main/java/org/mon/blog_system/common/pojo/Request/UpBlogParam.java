package org.mon.blog_system.common.pojo.Request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpBlogParam {
    @NotNull(message = "博客ID不能为空")
    private Integer id;
    @NotNull(message = "标题不能为空")
    private String title;
    @NotNull(message = "内容不能为空")
    private String content;
}
