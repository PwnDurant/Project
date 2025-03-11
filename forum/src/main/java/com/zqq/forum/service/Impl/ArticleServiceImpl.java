package com.zqq.forum.service.Impl;

import com.zqq.forum.common.AppResult;
import com.zqq.forum.common.ResultCode;
import com.zqq.forum.dao.ArticleMapper;
import com.zqq.forum.exception.ApplicationException;
import com.zqq.forum.model.Article;
import com.zqq.forum.model.Board;
import com.zqq.forum.model.User;
import com.zqq.forum.service.IArticleService;
import com.zqq.forum.service.IBoardService;
import com.zqq.forum.service.IUserService;
import com.zqq.forum.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private IBoardService boardService;
    @Autowired
    private IUserService userService;

    @Override
    public void create(Article article) {

        if(article==null||article.getUserId()==null||article.getBoardId()==null
        || StringUtil.isEmpty(article.getTitle())
        ||StringUtil.isEmpty(article.getContent())){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        article.setVisitCount(0);
        article.setReplyCount(0);
        article.setLikeCount(0);
        article.setDeleteState((byte)0);
        article.setState((byte)0);
        Date date=new Date();
        article.setCreateTime(date);
        article.setUpdateTime(date);

        int articleRow = articleMapper.insert(article);
        if(articleRow<=0){
            log.warn(ResultCode.FAILED_CREATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }

        User user = userService.selectById(article.getUserId());
        if(user==null){
            log.warn(ResultCode.FAILED_CREATE.toString()+",发帖失败,userId={}",article.getUserId());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        userService.addOneArticleCountById(user.getId());

        Board board = boardService.selectById(article.getBoardId());
        if(board==null){
            log.warn(ResultCode.FAILED_CREATE.toString()+",发帖失败,boardId={}",article.getBoardId());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        boardService.addOneArticleCountById(board.getId());

        log.info(ResultCode.SUCCESS.toString()+",userId={},boardId={},发帖成功，articleId={}",article.getUserId(),article.getBoardId(),article.getId());
//        throw new ApplicationException(AppResult.failed("测试事务回滚"));
    }

    @Override
    public List<Article> selectAll() {

        return articleMapper.selectAll();

    }

    @Override
    public List<Article> selectByBoardId(Long boardId) {

        if(boardId==null||boardId<=0){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        Board board=boardService.selectById(boardId);

        if(board==null){
            log.warn(ResultCode.FAILED_BOARD_NOT_EXISTS.toString()+",boardId={}",boardId);
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }
//        log.warn(board.toString());
        return articleMapper.selectByBoardId(boardId);

    }
}
