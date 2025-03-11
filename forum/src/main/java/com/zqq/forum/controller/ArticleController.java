package com.zqq.forum.controller;

import com.zqq.forum.common.AppConfig;
import com.zqq.forum.common.AppResult;
import com.zqq.forum.common.ResultCode;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

}
