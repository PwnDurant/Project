package com.zqq.blog_improve.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户信息实体
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfo extends BaseEntity {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 用户名字
     */
    private String userName;

    /**
     * 账号密码
     */
    private String password;

    /**
     * github 地址
     */
    private String githubUrl;

}
