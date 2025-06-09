package com.zqq.blog_improve.common.pojo.vo;

import lombok.Data;

@Data
public class UserInfoVO {

    /**
     * 返回的用户 id
     */
    private Long id;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * github 地址
     */
    private String githubUrl;

}
