package com.zqq.mybatisplus.domain.po;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {

    private Long id;

    private String username;

    private String password;

    private String phone;

    private String info;

    private Integer status;

    private Integer balance;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
