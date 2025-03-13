package com.zqq.forum.service.Impl;

import com.zqq.forum.common.AppResult;
import com.zqq.forum.common.ResultCode;
import com.zqq.forum.dao.ArticleReplyMapper;
import com.zqq.forum.exception.ApplicationException;
import com.zqq.forum.model.ArticleReply;
import com.zqq.forum.service.IArticleReplyService;
import com.zqq.forum.service.IArticleService;
import com.zqq.forum.utils.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ArticleReplyServiceImpl implements IArticleReplyService {

    @Autowired
    private ArticleReplyMapper articleReplyMapper;
    @Autowired
    private IArticleService articleService;

    @Override
    public void create(ArticleReply articleReply) {

        if(articleReply==null||articleReply.getArticleId()==null
        || StringUtil.isEmpty(articleReply.getContent()) ||articleReply.getPostUserId()==null){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        articleReply.setReplyId(null);
        articleReply.setReplyUserId(null);
        articleReply.setLikeCount(0);
        articleReply.setState((byte)0);
        articleReply.setDeleteState((byte)0);
        articleReply.setCreateTime(new Date());
        articleReply.setUpdateTime(new Date());

        int row = articleReplyMapper.insertSelective(articleReply);
        if(row!=1){
            log.warn(ResultCode.ERROR_SERVICES.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }

        articleService.addReplyCountById(articleReply.getArticleId());
        log.info("帖子回复成功，articleId={},userId={}",articleReply.getArticleId(),articleReply.getPostUserId());

    }

    @Override
    public List<ArticleReply> selectByArticleId(Long articleId) {

        if(articleId==null||articleId<=0){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        return  articleReplyMapper.selectByArticleId(articleId);

    }
}
