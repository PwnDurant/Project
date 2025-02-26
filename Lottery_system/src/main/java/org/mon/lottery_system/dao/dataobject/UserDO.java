package org.mon.lottery_system.dao.dataobject;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) //加上父类的属性
public class UserDO extends BaseDO  {

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
    private Encrypt phoneNumber;
    /**
     * 密码信息
     */
    private String password;
    /**
     * 身份信息
     */
    private String identity;
}
