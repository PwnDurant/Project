package com.zqq.blog_improve.common.constant;


public class Constants {

    /**
     * 密文长度
     */
    public static final Integer PASSWORD_LENGTH=64;

    /**
     * jwt 过期时间：1天
     */
    public static final Long EXPIRATION=60*60*24*1000L;

    public static final Long EXPIRATION_MIN = 60*24L;

    /**
     * token 在redis 中的key
     */
    public static final String LOGIN_TOKEN_KEY="loginToken:";

    /**
     * 载荷内容 key：userId
     */
    public static final String LOGIN_USER_ID="userId";

    /**
     * 载荷内容 key：userKey
     */
    public static final String LOGIN_USER_KEY="userKey";

    public static final String LOGIN_TOKEN_INFO = "用户 jwt 令牌信息";

    /**
     * 最低剩余过期时间
     */
    public static final Long REFRESH_TIME =180L;

    /**
     * 前端请求头中存放 token 的键值
     */
    public static final String USER_TOKEN = "user_token";

    /**
     * redis 这中博客信息
     */
    public static final String BLOG_LIST = "b:l:";
}
