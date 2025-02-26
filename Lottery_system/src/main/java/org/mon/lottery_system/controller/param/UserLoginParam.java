package org.mon.lottery_system.controller.param;

import lombok.Data;
import org.mon.lottery_system.service.enums.UserIdentityEnum;

import java.io.Serializable;


@Data
public class UserLoginParam implements Serializable {
    /**
     * 强制某身份登入
     * @see UserIdentityEnum#name()
     */
    private String mandatoryIdentity;
}
