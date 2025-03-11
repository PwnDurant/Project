package com.zqq.forum.service;


import com.zqq.forum.model.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IArticleService {

    /**
     * 发布帖子
     * @param article
     */
    @Transactional //当前方法需要被事务管理
    void create (Article article);

    /**
     * 查询所有文章列表
     * @return
     */
    List<Article> selectAll();

    /**
     * 根据板块Id来查询
     * @param boardId
     * @return
     */
    List<Article> selectByBoardId(Long boardId);

}
