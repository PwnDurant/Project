package com.zqq.blog_improve.common.constant;


public class Constants {

    /**
     * 密文长度
     */
    public static final Integer PASSWORD_LENGTH=64;

    /**
     * jwt 过期时间：1天
     */
    public static final Integer EXPIRATION=60*60*24;

    /**
     * token 在redis 中的key
     */
    public static final String LOGIN_TOKEN_KEY="loginToken";

    /**
     * 载荷内容 key：userId
     */
    public static final String LOGIN_USER_ID="userId";

    /**
     * 载荷内容 key：userKey
     */
    public static final String LOGIN_USER_KEY="userKey";

}
