package com.zqq.forum.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zqq.forum.model.Article;
import com.zqq.forum.service.IArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleServiceImplTest {


    @Autowired
    private IArticleService articleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() {
        Article article=new Article();
        article.setUserId(1L);
        article.setBoardId(1L);
        article.setTitle("单元测试");
        article.setContent("测试内容");
        articleService.create(article);
        System.out.println("发帖成功");
    }

    @Test
    void selectAll() throws JsonProcessingException {
        List<Article> articles = articleService.selectAll();
        System.out.println(objectMapper.writeValueAsString(articles));
    }

    @Test
    void selectByBoardId() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(articleService.selectByBoardId(10L)));
    }

    @Test
    void selectDetailById() throws JsonProcessingException {
        Article article = articleService.selectDetailById(1L);
        System.out.println(objectMapper.writeValueAsString(article));
    }

    @Test
    void modify() {
        articleService.modify(1L,"test111","test111");
    }

    @Test
    void selectById() throws JsonProcessingException {
        Article article=articleService.selectById(1L);
        System.out.println(objectMapper.writeValueAsString(article));
    }

    @Test
    void thumbsUpById() {
        articleService.thumbsUpById(1L);
    }

    @Test
    void deleteById() {
        articleService.deleteById(4L);
    }
}