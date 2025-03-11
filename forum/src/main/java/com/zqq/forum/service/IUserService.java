package com.zqq.forum.service;


import com.zqq.forum.model.User;

/**
 * 用户接口
 */
public interface IUserService {

    /**
     * 创建一个普通用户
     * @param user
     */
    void createNormalUser(User user);

}
