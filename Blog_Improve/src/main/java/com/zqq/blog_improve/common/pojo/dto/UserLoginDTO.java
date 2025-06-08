package com.zqq.blog_improve.common.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 用户登入需要提交的信息
 */
@Data
public class UserLoginDTO {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20,message = "用户名长度不能超过 20 位")
    private String userName;


    /**
     * 账号密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

}
