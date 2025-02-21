package org.mon.blog_system.common.pojo.Request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserLoginParam {

    @NotBlank(message = "用户名不能为空")
    @Length(max = 20,message = "用户名不能超过20")
    private String userName;

    @NotBlank(message = "密码不能为空")
    private String password;

}
