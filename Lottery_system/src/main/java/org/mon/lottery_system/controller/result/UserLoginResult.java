package org.mon.lottery_system.controller.result;


import lombok.Data;

@Data
public class UserLoginResult {
    /**
     * JWT 令牌
     */
    private String token;
    /**
     * 登入人员身份
     */
    private String identity;
}
