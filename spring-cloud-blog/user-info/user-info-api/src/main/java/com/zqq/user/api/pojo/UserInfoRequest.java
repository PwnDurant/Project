package com.zqq.user.api.pojo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserInfoRequest {
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名长度不能超过20")
    private String userName;

    @NotBlank(message = "密码不能为空")
    @Length(max = 20, message = "密码长度不能超过20")
    private String password;
}
