package org.mon.lottery_system.service.dto;


import lombok.Data;
import org.mon.lottery_system.dao.dataobject.Encrypt;
import org.mon.lottery_system.service.enums.UserIdentityEnum;

@Data
public class UserDTO {

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户电话
     */
    private String phoneNumber;
    /**
     * 身份信息
     */
    private UserIdentityEnum identity;

}
