package com.zqq.blog_improve.common.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态码
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS                     (1000,"操作成功！"),
    FAILED                      (2000,"服务器繁忙，请稍后重试！"),
    USER_NOT_EXISTS             (20001,"当前用户不存在,请注册！"),
    FAILED_PARAMS_VALIDATE      (20002,"输入参数校验异常！"),
    REQUEST_NOT_SUPPORT         (20003,"请求方式不支持！"),
    NAME_OR_PASSWORD_ERROR      (20004,"账号或密码错误！"),
    PASSWORD_IS_EMPTY           (20005,"密码不能为空！"),
    SQL_PASSWORD_IS_ERROR       (20006,"当前用户数据库中密码错误！" ),
    TOKEN_IS_EMPTY              (20007,"令牌不能为空！" ),
    TOKEN_IS_EXPIRED            (20008,"令牌过期！" ),
    TOKEN_SIGNATURE_ERROR       (20009,"令牌签名错误！" ),
    TOKEN_PARSE_ERROR           (20010,"令牌解析失败！" ),
    BLOG_IS_NOT_EXIST           (20011,"对应博客不存在！" ),
    BLOG_RESOURCES_IS_EMPTY     (20012,"数据库中博客资源为空！" ),
    TOKEN_ERROR                 (20013 ,"令牌不存在或已过期！");

    private final int code;

    private final String msg;

}
