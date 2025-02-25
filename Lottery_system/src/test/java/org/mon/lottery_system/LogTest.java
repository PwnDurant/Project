package org.mon.lottery_system;


import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
public class LogTest {

    private final static Logger logger= LoggerFactory.getLogger(LogTest.class);

    @Test
    void logTest(){
        System.out.println("hello world");
        logger.info("hello word");
    }
}
