package com.zqq.common.utils;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 加密解密类
 */
public class SecurityUtil {
    /**
     * 对密码进行加密
     * @return
     */
    public static String encrypt(String password){
        // 每次生成内容不同的，但长度固定 32 位的盐值
        String salt = UUID.randomUUID().toString().replace("-","");
        // 最终密码=md5(盐值+原始密码)
        String finalPassword = DigestUtils.md5DigestAsHex((salt+password).getBytes());
        return salt+finalPassword;
    }
    /**
     * 密码验证
     *
     * @param password      待验证密码
     * @param finalPassword 最终正确的密码（数据库中加盐的密码）
     * @return
     */
    public static boolean verify(String password,String finalPassword){
        //非空校验
        if (!StringUtils.hasLength(password) || !StringUtils.hasLength(finalPassword)){
            return false;
        }
        //最终密码不是64位, 则不正确
        if (finalPassword.length()!=64){
            return false;
        }
        //盐值
        String salt = finalPassword.substring(0,32);
        // 使用盐值+待确认的密码生成一个最终密码
        String securityPassword =
                DigestUtils.md5DigestAsHex((salt + password).getBytes());
        // 使用盐值+最终的密码和数据库的真实密码进行对比
        return (salt + securityPassword).equals(finalPassword);
    }

    public static void main(String[] args) {
        String finalPassword = encrypt("123456");
        System.out.println(finalPassword);
        System.out.println(verify("123456",finalPassword));

    }
}
