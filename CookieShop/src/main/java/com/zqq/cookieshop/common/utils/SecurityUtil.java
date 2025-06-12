package com.zqq.cookieshop.common.utils;


import com.zqq.cookieshop.common.base.ResultCode;
import com.zqq.cookieshop.common.constants.Constants;
import com.zqq.cookieshop.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;


/**
 * 密码认证工具包
 */
@Slf4j
public class SecurityUtil {

    /**
     * 对密码进行加密方法 -- 传统方法
     * @param password 需要加密的方法
     * @return 加密过后的密文
     */
    public static String encrypt_traditional(String password){
//        判断传入密码是否为空
        if(!StringUtils.hasLength(password)){
            throw new SystemException(ResultCode.PASSWORD_IS_EMPTY);
        }
//        生成 salt  --  32位盐值
        String salt = UUID.randomUUID().toString().replace("-", "");
//        md5（盐值+password）得到密文 得到32位16进制密文
        String md5DigestAsHex = DigestUtils.md5DigestAsHex((salt + password).getBytes(StandardCharsets.UTF_8));
//        最后放入数据库中密码为：盐值+密文 64位的最终密文 -- 这个可以任意拼接，只要最后自己记得怎么拼接然后可以取出 salt 就行
        return salt+md5DigestAsHex;
    }

    /**
     * 对用户传入的密码进程验证  -- 传统方法
     * @param inputPassword 用户输入的密码
     * @param sqlPassword 数据库中的密码
     * @return 是否相同
     */
    public static Boolean verify_traditional(String inputPassword,String sqlPassword){
//        参数校验
        if(!StringUtils.hasLength(inputPassword)){
            throw new SystemException(ResultCode.PASSWORD_IS_EMPTY);
        }
        if(!StringUtils.hasLength(sqlPassword)||sqlPassword.length()!= Constants.PASSWORD_LENGTH){
            throw new SystemException(ResultCode.SQL_PASSWORD_IS_ERROR);
        }
//        获取 salt
        String salt = sqlPassword.substring(0, 32);
//        md5（盐值+inputPassword）得到密文，得到 32 为 16进制密文
        String md5DigestAsHex = DigestUtils.md5DigestAsHex((salt + inputPassword).getBytes(StandardCharsets.UTF_8));
//        按照约定规则与数据库密文比对
        return (salt+md5DigestAsHex).equals(sqlPassword);
    }

    /**
     * 对密码进行加密  -- 用 spring-security-crypto 工具包
     * @param password 需要加密的密码
     * @return 加密过后的密文 60位 -- 自动加入随机盐值，每次生成的密文都不一样
     */
    public static String encrypt_new(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    /**
     * 对密码进行比对
     * @param inputPassword 用户输入密码
     * @param sqlPassword 数据库密码
     * @return 是否相同 -- 即使每次加密结果不一样，但是调用 matches 方法比对的结果还是一样的
     */
    public static Boolean verify_new(String inputPassword,String sqlPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(inputPassword,sqlPassword);
    }

}
