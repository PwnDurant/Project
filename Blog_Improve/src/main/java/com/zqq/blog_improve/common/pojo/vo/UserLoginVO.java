package com.zqq.blog_improve.common.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 登入成功之后返回给前端的 entity 类
 */
@Data
public class UserLoginVO {

    /**
     * 用户 Id
     */
    private Long  userId;

    /**
     * 登入成功之后返回的 token 令牌
     */
    private String token;

}
