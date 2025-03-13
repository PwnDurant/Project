package com.zqq.forum.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zqq.forum.model.Message;
import com.zqq.forum.service.IMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageServiceImplTest {

    @Autowired
    private IMessageService messageService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void create() {
        Message message=new Message();
        message.setPostUserId(3L);
        message.setReceiveUserId(1L);
        message.setContent("单元回复测试");
        messageService.create(message);

    }

    @Test
    void selectCountById() {
        System.out.println(messageService.selectCountById(9L));
    }

    @Test
    void selectByReceiveId() throws JsonProcessingException {
        List<Message> messages = messageService.selectByReceiveId(1L);
        System.out.println(objectMapper.writeValueAsString(messages));
    }

    @Test
    @Transactional
    void updateStateById() {
        messageService.updateStateById(1L,(byte)0);
    }

    @Test
    void selectById() {
        System.out.println(messageService.selectById(1L));
    }

    @Test
    void reply() {
        Message message=new Message();
        message.setPostUserId(1L);
        message.setReceiveUserId(3L);
        message.setContent("cehsihuifu");
        messageService.reply(2L,message);
    }
}