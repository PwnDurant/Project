package com.zqq.forum.model;

import lombok.Data;

import java.util.Date;

@Data
public class Board {
    private Long id;

    private String name;

    private Integer articleCount;

    private Integer sort;

    private Byte state;

    private Byte deleteState;

    private Date createTime;

    private Date updateTime;

}