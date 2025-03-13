package com.zqq.forum.controller;

import com.zqq.forum.common.AppConfig;
import com.zqq.forum.common.AppResult;
import com.zqq.forum.common.ResultCode;
import com.zqq.forum.model.Article;
import com.zqq.forum.model.ArticleReply;
import com.zqq.forum.model.User;
import com.zqq.forum.service.IArticleReplyService;
import com.zqq.forum.service.IArticleService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reply")
public class ArticleReplyController {

    @Autowired
    private IArticleService articleService;
    @Autowired
    private IArticleReplyService articleReplyService;

    @PostMapping("create")
    public AppResult create(HttpServletRequest request,
                            @RequestParam("articleId") @Nonnull Long articleId,
                            @RequestParam("content") @Nonnull String content){

        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute(AppConfig.USER_SESSION);
        if(user.getState()==1){
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }

        Article article = articleService.selectById(articleId);
        if(article==null||article.getDeleteState()==1){
            return AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS);
        }
        if(article.getState()==1){
            return AppResult.failed(ResultCode.FAILED_ARTICLE_BANNED);
        }

        ArticleReply articleReply=new ArticleReply();
        articleReply.setArticleId(articleId);
        articleReply.setPostUserId(user.getId());
        articleReply.setContent(content);
        articleReplyService.create(articleReply);

        return AppResult.success();

    }

    @GetMapping("/getReplies")
    public AppResult<List<ArticleReply>> getRepliedArticleId(@RequestParam("articleId") @Nonnull Long articleId){

        Article article = articleService.selectById(articleId);
        if(article==null||article.getDeleteState()==1){
            return AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS);
        }

        List<ArticleReply> articleReplies = articleReplyService.selectByArticleId(articleId);

        return AppResult.success(articleReplies);
    }

}
