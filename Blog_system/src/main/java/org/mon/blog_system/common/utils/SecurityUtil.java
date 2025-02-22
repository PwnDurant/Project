package org.mon.blog_system.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.mon.blog_system.common.exception.BlogException;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;


@Slf4j
public class SecurityUtil {
//    加密方法
//    根据密码返回密文加盐值
    public static String encrypt(String password){
        if(!StringUtils.hasLength(password)) throw new BlogException("密码不能为空");

//        生成盐值
        String salt= UUID.randomUUID().toString().replace("-","");

//        md5(盐值+password) 得到密文 得到的是32位16进制的密文
        String securityPassword = DigestUtils.md5DigestAsHex((salt + password).getBytes(StandardCharsets.UTF_8));

//        数据库中应该保存密文加盐值 得到一个64位16进制的数据
        return salt+securityPassword;

    }

//    验证方法 从数据库中取出盐值，然后再与用户输入的密码进行md5加密，然后再判断是否相等
    public static Boolean verify(String inputPassword,String sqlPassword){
//        参数校验
        if(!StringUtils.hasLength(inputPassword)||!StringUtils.hasLength(sqlPassword)) return false;
        if(sqlPassword.length()!=64) return false;

//        获取salt
        String salt=sqlPassword.substring(0,32);

//        md5(盐值+password) 得到密文 得到的是32位16进制的密文
        String securityPassword = DigestUtils.md5DigestAsHex((salt + inputPassword).getBytes(StandardCharsets.UTF_8));

        return (salt+securityPassword).equals(sqlPassword);

    }
}
