package com.zqq.forum.model;

import lombok.Data;

import java.util.Date;


@Data
public class Message {
    private Long id;

    private Long postUserId;

    private Long receiveUserId;

    private String content;

    private Byte state;

    private Byte deleteState;

    private Date createTime;

    private Date updateTime;

}