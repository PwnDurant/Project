package com.zqq.forum.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zqq.forum.model.Board;
import com.zqq.forum.service.IBoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceImplTest {

    @Autowired
    private IBoardService boardServiceImpl;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void selectByNum() {
        System.out.println(boardServiceImpl.selectByNum(5));
    }

    @Test
    @Transactional
    void addOneArticleCountById() {
        boardServiceImpl.addOneArticleCountById(1L);
        System.out.println("success");
    }

    @Test
    void selectById() throws JsonProcessingException {
        Board board=boardServiceImpl.selectById(10L);
        System.out.println(objectMapper.writeValueAsString(board));
    }
}