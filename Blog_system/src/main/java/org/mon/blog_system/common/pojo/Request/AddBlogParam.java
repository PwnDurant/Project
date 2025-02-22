package org.mon.blog_system.common.pojo.Request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;


//添加博客传递的参数
@Data
public class AddBlogParam {

    @NotNull(message = "作者ID不能为空")
    private Integer userId;
    @NotNull(message = "标题不能为空")
    private String title;
    @NotNull(message = "内容不能为空")
    private String content;
}
