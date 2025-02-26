package org.mon.lottery_system;


import org.junit.jupiter.api.Test;
import org.mon.lottery_system.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VerificationCodeServiceTest {
    @Autowired
    private VerificationCodeService verificationCodeService;

    @Test
    void testSend(){
        verificationCodeService.sendVerificationCode("19844593150");
    }

    @Test
    void testGet(){
        System.out.println(verificationCodeService.getVerificationCode("19844593150"));
    }

}
