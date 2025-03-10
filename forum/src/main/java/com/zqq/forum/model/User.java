package com.zqq.forum.model;

import lombok.Data;

import java.util.Date;


@Data
public class User {
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String phoneNum;

    private String email;

    private Byte gender;

    private String salt;

    private String avatarUrl;

    private Integer articleCount;

    private Byte isAdmin;

    private String remark;

    private Byte state;

    private Byte deleteState;

    private Date createTime;

    private Date updateTime;

}