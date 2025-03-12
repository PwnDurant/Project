package com.zqq.forum.model;

import lombok.Data;

import java.util.Date;

@Data
public class Article {
    private Long id;

    private Long boardId;

    private Long userId;

    private String title;

    private Integer visitCount;

    private Integer replyCount;

    private Integer likeCount;

    private Byte state;

    private Byte deleteState;

    private Date createTime;

    private Date updateTime;

    private String content;

    private User user;

    private Board board;

    private boolean isOwn;

}