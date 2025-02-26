package org.mon.lottery_system.service.dto;


import lombok.Data;
import org.mon.lottery_system.service.enums.UserIdentityEnum;

@Data
public class UserLoginDTO {
    /**
     * JWT 令牌
     */
    private String token;
    /**
     * 登入人员身份
     */
    private UserIdentityEnum identity;
}
