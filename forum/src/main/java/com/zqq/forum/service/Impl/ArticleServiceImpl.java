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

    @Override
    public Article selectDetailById(Long id) {

        if(id==null||id<=0){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }

        Article article = articleMapper.selectDetailById(id);
        if(article==null){
            log.warn(ResultCode.FAILED_ARTICLE_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS));
        }

        Article updateArticle=new Article();
        updateArticle.setId(article.getId());
        updateArticle.setVisitCount(article.getVisitCount()+1);

        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        if(row!=1){
            log.warn(ResultCode.ERROR_SERVICES.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }

        article.setVisitCount(article.getVisitCount()+1);

        return article;
    }

    @Override
    public void modify(Long id, String title, String content) {

        if(id==null||id<=0||StringUtil.isEmpty(title)||StringUtil.isEmpty(content)){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        Article updateArticle=new Article();
        updateArticle.setId(id);
        updateArticle.setTitle(title);
        updateArticle.setContent(content);
        updateArticle.setUpdateTime(new Date());

        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        if(row!=1){
            log.warn(ResultCode.ERROR_SERVICES.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }

    }

    @Override
    public Article selectById(Long id) {

        if(id==null||id<=0){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }

        return articleMapper.selectByPrimaryKey(id);

    }

    @Override
    public void thumbsUpById(Long id) {

        if(id==null||id<=0){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }

        Article article = articleMapper.selectByPrimaryKey(id);
        if(article==null||article.getDeleteState()==1){
            log.warn(ResultCode.FAILED_ARTICLE_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS));
        }

        if(article.getState()==1){
            log.warn(ResultCode.FAILED_ARTICLE_BANNED.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_BANNED));
        }

        Article updateArticle=new Article();
        updateArticle.setId(article.getId());
        updateArticle.setLikeCount(article.getLikeCount()+1);
        updateArticle.setUpdateTime(new Date());

        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        if(row!=1){
            log.warn(ResultCode.ERROR_SERVICES.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }


    }

    @Override
    public void deleteById(Long id) {

        if(id==null||id<=0){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }

        Article article = articleMapper.selectByPrimaryKey(id);
        if(article==null||article.getDeleteState()==1){
            log.warn(ResultCode.FAILED_ARTICLE_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS));
        }

        Article updateArticle=new Article();
        updateArticle.setId(article.getId());
        updateArticle.setDeleteState((byte)1);

        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        if(row!=1){
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }

        boardService.subOneArticleCountById(article.getBoardId());
        userService.subOneArticleCountById(article.getUserId());
        log.info("删除帖子成功，article={},userId={}",article.getId(),article.getUserId());

    }

    @Override
    public void addReplyCountById(Long id) {

        if(id==null||id<=0){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }

        Article article = articleMapper.selectByPrimaryKey(id);
        if(article==null||article.getDeleteState()==1){
            log.warn(ResultCode.FAILED_ARTICLE_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS));
        }

        if(article.getState()==1){
            log.warn(ResultCode.FAILED_ARTICLE_BANNED.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_BANNED));
        }

        Article updateArticle=new Article();
        updateArticle.setId(article.getId());
        updateArticle.setReplyCount(article.getReplyCount()+1);
        updateArticle.setUpdateTime(new Date());

        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        if(row!=1){
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }

    }

    @Override
    public List<Article> selectByUserId(Long userId) {

        if(userId==null||userId<=0){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }

        User user = userService.selectById(userId);
        if(user==null){
            log.warn(ResultCode.FAILED_USER_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }

        return  articleMapper.selectByUserId(userId);

    }

}
