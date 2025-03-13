package com.zqq.forum.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zqq.forum.model.ArticleReply;
import com.zqq.forum.service.IArticleReplyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleReplyServiceImplTest {

    @Autowired
    private IArticleReplyService articleReplyService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void create() {
        ArticleReply articleReply=new ArticleReply();
        articleReply.setArticleId(3L);
        articleReply.setPostUserId(1L);
        articleReply.setContent("单元测试回复");
        articleReplyService.create(articleReply);
        System.out.println("回复成功");
    }

    @Test
    void selectByArticleId() throws JsonProcessingException {
        List<ArticleReply> articleReplies = articleReplyService.selectByArticleId(6L);
        System.out.println(objectMapper.writeValueAsString(articleReplies));
    }
}