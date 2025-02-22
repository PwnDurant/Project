package org.mon.blog_system;


import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class SecurityUtilTest {
    @Test
    void encrypt(){
//        加密
        String password="123456";
        String s = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        System.out.println(s); //通过md5进行加密后的结果

//        生成盐值
//        uuid：随机字符串，保证出现重复的概率是极低的，一共是32位，每一位是16进制：1/32^16次方
        String uuid=UUID.randomUUID().toString();
        System.out.println(uuid);

    }
}
