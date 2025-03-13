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

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User selectByUserName(String username);


    /**
     * 处理用户登入
     * @param username
     * @param password
     * @return
     */
    User login(String username,String password);


    /**
     * 根据Id去查询用户信息
     * @param id
     * @return
     */
    User selectById(Long id);


    /**
     * 更新当前用户的发帖数+1
     * @param id
     */
    void addOneArticleCountById(Long id);


    /**
     * 更新当前用户的发帖数-1
     * @param id
     */
    void subOneArticleCountById(Long id);

    /**
     * 修改个人信息
     * @param user 要修改的对象
     */
    void modifyInfo(User user);


    /**
     * 修改密码
     * @param id
     * @param newPassword
     * @param oldPassword
     */
    void modifyPassword(Long id,String newPassword,String oldPassword);



}
