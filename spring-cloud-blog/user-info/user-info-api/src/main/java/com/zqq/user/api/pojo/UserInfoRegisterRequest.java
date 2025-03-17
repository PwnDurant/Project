package com.zqq.user.api.pojo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserInfoRegisterRequest {

    @NotBlank(message = "用户名不能为空")
    @Length(max = 20,message = "用户名不能超过20")
    private String userName;

    @NotBlank(message = "密码不能为空")
    @Length(max = 20,message = "密码不能超过20")
    private String password;

    @Length(max = 64,message = "githubUrl不能超过64")
    private String githubUrl;

    @NotBlank(message = "邮箱不能为空")
    @Length(max = 20,message = "邮箱不能超过20")
    private String email;


}
