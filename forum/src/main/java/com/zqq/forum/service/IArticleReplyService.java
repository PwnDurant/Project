package com.zqq.forum.service;

import com.zqq.forum.model.ArticleReply;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IArticleReplyService {

    /**
     * 新增帖子回复
     * @param articleReply
     */
    @Transactional
    void create(ArticleReply articleReply);

    /**
     * 根据帖子Id查询所有的回复
     * @param articleId
     * @return
     */
    List<ArticleReply> selectByArticleId (Long articleId);

}
