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

    /**
     * 根据帖子Id查询详情
     * @param id
     * @return
     */
    Article selectDetailById( Long id);

    /**
     * 根据帖子Id更新帖子标题和内容
     * @param id
     * @param title
     * @param content
     */
    void modify(Long id,String title,String content);

    /**
     * 根据id去查询帖子信息
     * @param id
     * @return
     */
    Article selectById(Long id);

    /**
     * 点赞帖子
     * @param id 帖子Id
     */
    void thumbsUpById(Long id);

    /**
     * 删除帖子
     * @param id
     */
    @Transactional
    void deleteById(Long id);

    /**
     * 更新文章回复数量+1
     * @param id
     */
    void addReplyCountById(Long id);


    /**
     * 根据用户Id去查询
     * @return
     */
    List<Article> selectByUserId(Long userId);

}
