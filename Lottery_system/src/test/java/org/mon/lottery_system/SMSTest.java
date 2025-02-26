package org.mon.lottery_system;


import org.junit.jupiter.api.Test;
import org.mon.lottery_system.common.utils.SMSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SMSTest {

    @Autowired
    private SMSUtil smsUtil;

    @Test
    void smsTest(){
        smsUtil.sendMessage("SMS_479100562","19844593150","{\"code\":\"1234\"}");
//        {"code":"1234"}
    }

}
