package org.mon.lottery_system;


import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Key;

@SpringBootTest
public class JWTTest {

    @Test
    public void genKey(){
//        创建一个密钥对象，使用HS256签名算法
        Key key= Keys.secretKeyFor(SignatureAlgorithm.HS256);
//        将密钥编码为Base64字符串
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println(secretString);
//        结果：mXsryY84BPeVtSuYkdhdwwAwi2KOOx8lVby0KLJ1Ljg=
    }
}
