package com.zqq.cookieshop.controller.user.vo;

import lombok.Data;

/**
 * 用户登入成功返回信息
 */
@Data
public class UserVO {

    private Long userId;

    private String token;

}
