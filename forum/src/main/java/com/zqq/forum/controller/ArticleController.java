package com.zqq.forum.controller;

import com.zqq.forum.common.AppConfig;
import com.zqq.forum.common.AppResult;
import com.zqq.forum.common.ResultCode;
import com.zqq.forum.exception.ApplicationException;
import com.zqq.forum.model.Article;
import com.zqq.forum.model.Board;
import com.zqq.forum.model.User;
import com.zqq.forum.service.IArticleService;
import com.zqq.forum.service.IBoardService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {


    @Autowired
    private IBoardService boardService;
    @Autowired
    private IArticleService articleService;


    /**
     * 发布新帖
     * @param boardId
     * @param title
     * @param content
     * @return
     */
    @RequestMapping("/create")
    public AppResult create(@RequestParam("boardId") @Nonnull Long boardId,
                            @RequestParam("title") @Nonnull String title,
                            @RequestParam("content") @Nonnull String content,
                            HttpServletRequest request){

        HttpSession session= request.getSession(false);
        User user=(User) session.getAttribute(AppConfig.USER_SESSION);
        if(user.getState()==1){
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }

        Board board = boardService.selectById(boardId.longValue());
        if(board==null||board.getDeleteState()==1||board.getState()==1){
            log.warn(ResultCode.FAILED_BOARD_BANNED.toString());
            return AppResult.failed(ResultCode.FAILED_BOARD_BANNED);
        }

        Article article=new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setBoardId(boardId);
        article.setUserId(user.getId());
        articleService.create(article);

        return AppResult.success();
    }

    @GetMapping("/getAllByBoardId")
    public AppResult<List<Article>> getAllByBoardId(@RequestParam(value = "boardId" ,required = false) Long boardId){

        List<Article> articles;

        if(boardId==null){
            articles = articleService.selectAll();
        }else{
            articles=articleService.selectByBoardId(boardId);
        }

        if(articles==null){
            articles=new ArrayList<>();
        }

        return AppResult.success(articles);
    }

    @GetMapping("/details")
    public AppResult<Article> getDetails(@RequestParam("id") @Nonnull Long id , HttpServletRequest request){

        HttpSession session= request.getSession(false);
        User user=(User) session.getAttribute(AppConfig.USER_SESSION);

        Article article = articleService.selectDetailById(id);

        if(article==null){
            log.warn(ResultCode.FAILED_ARTICLE_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS));
        }

        if(Objects.equals(user.getId(), article.getUserId())){
            article.setOwn(true);
        }

        return AppResult.success(article);
    }


    @PostMapping("/modify")
    public AppResult modify(@RequestParam("id") @Nonnull Long id,
                            @RequestParam("title") @Nonnull String title,
                            @RequestParam("content") @Nonnull String content,
                            HttpServletRequest request){

        HttpSession session= request.getSession(false);
        User user= (User) session.getAttribute(AppConfig.USER_SESSION);

        if(user.getState()==1){
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }

        Article article=articleService.selectById(id);
        if(article==null){
            return AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS);
        }

        if(user.getId()!= article.getUserId()){
            return AppResult.failed(ResultCode.FAILED_FORBIDDEN);
        }

        if(article.getState()==1||article.getDeleteState()==1){
            return AppResult.failed(ResultCode.FAILED_ARTICLE_BANNED);
        }

        articleService.modify(id,title,content);
        log.info("帖子更新成功,articleId={},userId={}.",id,user.getId());

        return AppResult.success();
    }

    @PostMapping("/thumbsUp")
    public AppResult thumbsUp(@RequestParam("id") @Nonnull Long id,
                              HttpServletRequest request){

        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute(AppConfig.USER_SESSION);
        if(user.getState()==1){
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }

        articleService.thumbsUpById(id);

        return AppResult.success();

    }

    @PostMapping("/delete")
    public AppResult deleteById(@RequestParam("id") @Nonnull Long id,
                                HttpServletRequest request){

        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute(AppConfig.USER_SESSION);
        if(user.getState()==1){
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }

        Article article = articleService.selectById(id);
        if(article==null||article.getDeleteState()==1){
            return AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS);
        }

        if(user.getId()!= article.getUserId()){
            return AppResult.failed(ResultCode.FAILED_FORBIDDEN);
        }

        articleService.deleteById(id);

        return AppResult.success();
    }


    @GetMapping("/getAllByUserId")
    public AppResult<List<Article>> getAllByUserId(@RequestParam(value = "userId" ,required = false)  Long userId,
                                                   HttpServletRequest request){

        if(userId==null){
            HttpSession session = request.getSession(false);
            User user = (User)session.getAttribute(AppConfig.USER_SESSION);
            userId=user.getId();
        }

        List<Article> articles = articleService.selectByUserId(userId);
        log.info("查询帖子列表,userId={}",userId);

        return AppResult.success(articles);

    }



}
