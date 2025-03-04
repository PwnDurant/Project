package org.mon.lottery_system;


import org.junit.jupiter.api.Test;
import org.mon.lottery_system.common.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailTest {

    @Autowired
    private MailUtil mailUtil;

    @Test
    void sendMessage(){
        mailUtil.sendSampleMail("1781389019@qq.com","test","test");
    }

}
