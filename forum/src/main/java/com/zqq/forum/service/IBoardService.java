package com.zqq.forum.service;


import com.zqq.forum.model.Board;

import java.util.List;

public interface IBoardService {

    /**
     * 查询num条记录
     * @param num
     * @return
     */
    List<Board> selectByNum (Integer num);

    /**
     * 更新板块的发帖数
     * @param id
     */
    void addOneArticleCountById(Long id);

    /**
     * 根据板块id查询板块信息
     * @param id 板块id
     * @return
     */
    Board selectById(Long id);
}
