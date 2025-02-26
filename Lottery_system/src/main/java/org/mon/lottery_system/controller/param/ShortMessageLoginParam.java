package org.mon.lottery_system.controller.param;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ShortMessageLoginParam extends UserLoginParam{
    /**
     * 注册手机号
     */
    @NotBlank(message = "电话不能为空！")
    private String loginMobile;
    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空！")
    private String verificationCode;
}
