package com.zqq.forum.dao;

import com.zqq.forum.model.Article;
import com.zqq.forum.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface ArticleMapper {
    int insert(Article row);

    int insertSelective(Article row);

    Article selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Article row);

    int updateByPrimaryKeyWithBLOBs(Article row);

    int updateByPrimaryKey(Article row);

    /**
     * 查询所有文章列表
     * @return
     */
    List<Article> selectAll();

    /**
     * 更具板块Id来查询
     * @param boardId
     * @return
     */
    List<Article> selectByBoardId(@Param("boardId") Long boardId);


    /**
     * 根据帖子Id查询详情
     * @param id
     * @return
     */
    Article selectDetailById(@Param("id") Long id);

    /**
     * 根据用户Id去查询
     * @return
     */
    List<Article> selectByUserId(@Param("userId") Long userId);

}