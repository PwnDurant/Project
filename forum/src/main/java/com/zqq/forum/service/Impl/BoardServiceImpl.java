package com.zqq.forum.service.Impl;

import com.zqq.forum.common.AppResult;
import com.zqq.forum.common.ResultCode;
import com.zqq.forum.dao.BoardMapper;
import com.zqq.forum.exception.ApplicationException;
import com.zqq.forum.model.Board;
import com.zqq.forum.service.IBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class BoardServiceImpl implements IBoardService {

    @Autowired
    private BoardMapper boardMapper;

    @Override
    public List<Board> selectByNum(Integer num) {
        if(num<=0){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        return boardMapper.selectByNum(num);
    }

    @Override
    public void addOneArticleCountById(Long id) {

        if(id==null||id<=0){
            log.warn(ResultCode.FAILED_BOARD_ARTICLE_COUNT.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_ARTICLE_COUNT));
        }

        Board board = boardMapper.selectByPrimaryKey(id);
        if (board == null) {
            log.warn(ResultCode.ERROR_IS_NULL.toString()+",board id = "+id);
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }

//        设置要更新的属性进行更新，不需要全部更新
        Board updateBoard=new Board();
        updateBoard.setId(board.getId());
        updateBoard.setArticleCount(board.getArticleCount()+1);
        int row = boardMapper.updateByPrimaryKeySelective(updateBoard);
        if(row!=1){
            log.warn(ResultCode.ERROR_SERVICES.toString()+"受影响行数不等于1.");
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }
    }

    @Override
    public Board selectById(Long id) {
        if(id==null||id<=0){
            log.warn(ResultCode.FAILED_BOARD_ARTICLE_COUNT.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_ARTICLE_COUNT));
        }
        return boardMapper.selectByPrimaryKey(id);
    }

    @Override
    public void subOneArticleCountById(Long id) {

        if(id==null||id<=0){
            log.warn(ResultCode.FAILED_BOARD_ARTICLE_COUNT.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_ARTICLE_COUNT));
        }

        Board board = boardMapper.selectByPrimaryKey(id);
        if (board == null) {
            log.warn(ResultCode.FAILED_BOARD_NOT_EXISTS.toString()+",board id = "+id);
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }

        Board updateBoard=new Board();
        updateBoard.setId(board.getId());
        updateBoard.setArticleCount(board.getArticleCount()-1);

        if(updateBoard.getArticleCount()<0){
            updateBoard.setArticleCount(0);
        }

        int row = boardMapper.updateByPrimaryKeySelective(updateBoard);
        if(row!=1){
            log.warn(ResultCode.FAILED.toString()+"受影响行数不等于1.");
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }

    }
}
