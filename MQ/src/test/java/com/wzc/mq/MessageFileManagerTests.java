package com.wzc.mq;

import com.wzc.mq.mqserver.datacenter.MessageFileManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 测试消息持久化的操作类
 */
@SpringBootTest
public class MessageFileManagerTests {

    private MessageFileManager messageFileManager = new MessageFileManager();

    /**
     * 每个用例执行之前的准备工作
     */
    @BeforeEach
    public void setUp(){

    }

    /**
     * 每个用例执行之后的收尾工作
     */
    @AfterEach
    public void tearDown(){

    }

}
