package org.mon.blog_system;


import org.junit.jupiter.api.Test;
import org.mon.blog_system.common.utils.SecurityUtil;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class SecurityUtilTest {
    @Test
    void encrypt(){
//        加密
        String password="123456";
//        String s = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
//        System.out.println(s); //通过md5进行加密后的结果

//        生成盐值
//        uuid：随机字符串，保证出现重复的概率是极低的，一共是32位，每一位是16进制：1/32^16次方
//        这是根据MAC地址进行生成,每一台机器的MAC的地址都不一样，平台会将uuid和mac地址进行绑定，可以直接查到
//        也可以根据时间戳进行生成
//        String uuid=UUID.randomUUID().toString();
//        System.out.println(uuid);

        String uuid=UUID.randomUUID().toString().replace("-","");
        System.out.println(uuid);//8d62381cb7e04b3089b989412709b0f4123456  盐值   需要存入盐值到数据库 进行后面解密。存入的话就把盐值和密文放在一起存（自行拼接）

//        将uuid和生成的md5进行拼接，然后再进行加密，然后再进行存储
//        或者将明文和uuid拼接之后再进行加密
        String finalPassword=uuid+password;   //盐值+明文得到一个较为复杂的密码
        String s = DigestUtils.md5DigestAsHex(finalPassword.getBytes(StandardCharsets.UTF_8));  //021f03295c6937c692b6e1b11a5fdd29     这个是秘文，也需要存入数据库
        System.out.println(s);

//        存储盐值加密文，盐值+密文or else

    }

    @Test
    void genPassword(){
        String encrypt = SecurityUtil.encrypt("zyp123");
        System.out.println(encrypt);
    }
    //    zqq1314ljm ljm1314 zyp123
}
