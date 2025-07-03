package com.wzc.mq;

import com.wzc.mq.mqserver.datacenter.MessageFileManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * 测试消息持久化的操作类
 */
@SpringBootTest
public class MessageFileManagerTests {

    private final MessageFileManager messageFileManager = new MessageFileManager();

    private static final String queueName1 = "testQueue1";
    private static final String queueName2 = "testQueue2";

    /**
     * 每个用例执行之前的准备工作，
     * 用来创建两个消息队列
     */
    @BeforeEach
    public void setUp() throws IOException {
        messageFileManager.createQueueFiles(queueName1);
        messageFileManager.createQueueFiles(queueName2);
    }

    /**
     * 每个用例执行之后的收尾工作
     * 把刚刚创建的两个队列删除
     */
    @AfterEach
    public void tearDown() throws IOException {
        messageFileManager.destroyQueueFiles(queueName1);
        messageFileManager.destroyQueueFiles(queueName2);
    }

    /**
     * 测试创建出来的队列的对应的文件有没有被创建出来
     */
    @Test
    public void testCreateFiles(){

    }

}
