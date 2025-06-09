package com.zqq.blog_improve.controller;

import com.zqq.blog_improve.common.base.R;
import com.zqq.blog_improve.common.pojo.dto.UserLoginDTO;
import com.zqq.blog_improve.common.pojo.vo.UserInfoVO;
import com.zqq.blog_improve.common.pojo.vo.UserLoginVO;
import com.zqq.blog_improve.service.IUserService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource(name = "userServiceImpl")
    private IUserService userService;

    /**
     * 处理用户登入请求
     * @param userLoginDTO 登入需要携带的数据：账号名和密码
     * @return 返回是否登入成功
     */
    @PostMapping("/login")
    public R<UserLoginVO> login(@RequestBody @Validated UserLoginDTO userLoginDTO){
        log.info("用户登入,userName:{}",userLoginDTO.getUserName());
        return R.ok(userService.login(userLoginDTO));
    }

    /**
     * 处理用户注册请求
     * @param userLoginDTO 注册需要携带的数据：账号名和密码
     * @return 是否注册成功
     */
    @PostMapping("/register")
    public R<Boolean> register(@RequestBody @Validated UserLoginDTO userLoginDTO){
        log.info("用户注册,userName:{}",userLoginDTO.getUserName());
        return R.ok(userService.register(userLoginDTO));
    }

    /**
     * 获得用户信息
     * @param userId 用户Id
     * @return 返回用户信息
     */
    @GetMapping("/getUserInfo")
    public R<UserInfoVO> getUserInfo(@NotNull Integer userId){
        log.info("获取用户信息,userId:{}",userId);
        return userService.getUserInfoById(userId);
    }

    /**
     * 获取作者信息
     * @param blogId 博客 id
     * @return 返回对应博客id 的用户信息
     */
    @GetMapping("/getAuthorInfo")
    public R<UserInfoVO> getAuthorInfo(@NotNull Integer blogId){
        log.info("获取作者信息,blogId:{}",blogId);
        return userService.getAuthorInfoById(blogId);
    }




}
