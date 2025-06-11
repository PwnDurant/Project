package com.zqq.cookieshop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 用户实体类
 */
@Data
public class User {

    /**
     * 用户ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户手机
     */
    private String phone;

    /**
     * 用户地址
     */
    private String address;

    /**
     * 是否是管理员用户
     */
    private Integer isAdmin;

    /**
     * 是否合法用户
     */
    private Integer isValidate;

}
