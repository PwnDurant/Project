package com.zqq.api.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpBlogRequest {
    @NotNull(message = "博客ID不能为空")
    private Integer id;
    private String title;
    private String content;

}
