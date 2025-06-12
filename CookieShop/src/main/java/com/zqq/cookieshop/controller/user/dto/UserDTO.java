package com.zqq.cookieshop.controller.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户登入信息
 */
@Data
public class UserDTO {

    @NotBlank(message = "用户账号不能为空！")
    @Length(max = 20,message = "用户名长度不能超过20位")
    private String userName;

    @NotBlank(message = "密码不能为空！")
    private String password;

}
