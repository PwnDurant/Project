package com.zqq.blog_improve.controller;

import com.zqq.blog_improve.common.base.R;
import com.zqq.blog_improve.common.pojo.dto.UserLoginDTO;
import com.zqq.blog_improve.common.pojo.vo.UserLoginVO;
import com.zqq.blog_improve.service.IUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
