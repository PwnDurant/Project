package org.mon.lottery_system;


import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.AES;
import org.apache.catalina.security.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.security.Security;


@SpringBootTest
public class EncryptTest {

//    密码 hash sha256
    @Test
    void sha256Test(){
        String encrypt = DigestUtil.sha256Hex("123456");
        System.out.println("经过sha256处理后的结果是"+encrypt);
//        8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92
    }

//    手机号 对称加密 aes
    @Test
    void aesTest(){
//        密钥  对于密钥的长度必须是16位
        byte[] key="123456789abcdefg".getBytes(StandardCharsets.UTF_8);

//        加密
//        aes对象
        AES aes = SecureUtil.aes(key);

        String s = aes.encryptHex("123456789");
        System.out.println("对称加密的结果为:  "+s);

//        解密
        System.out.println("经过aes解密后的结果:"+aes.decryptStr(s));

    }
}
