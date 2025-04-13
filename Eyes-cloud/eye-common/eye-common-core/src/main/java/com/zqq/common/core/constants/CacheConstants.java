package com.zqq.common.core.constants;

public class CacheConstants {

    public final static String LOGIN_TOKEN_KEY="loginToken:";  //token信息

    public final static Long EXP=720L;  //720分钟的过期时间

    public static final Long REFRESH_TIME = 180L;  //如果用户的token有效时常还剩180分钟的话就进行延长

    public static final String USER_UPLOAD_TIMES_KEY = "u:u:t";  //用户图片上传次数

    public static final String PHONE_CODE_KEY="p:c:";  //验证码code的redis结构

    public static final String CODE_TIME_KEY="c:t:";   //当天发送次数

    public final static String USER_DETAIL = "u:d:";   //用户详情信息

    public final static long USER_EXP = 10;  //将在缓存中的用户信息延长10min





}
