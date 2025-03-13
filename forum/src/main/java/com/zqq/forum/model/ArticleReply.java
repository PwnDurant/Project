package com.zqq.forum.model;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleReply {
    private Long id;

    private Long articleId;

    private Long postUserId;

    private Long replyId;

    private Long replyUserId;

    private String content;

    private Integer likeCount;

    private Byte state;

    private Byte deleteState;

    private Date createTime;

    private Date updateTime;

    private User user;

}