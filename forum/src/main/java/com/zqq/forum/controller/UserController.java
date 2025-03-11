package com.zqq.forum.controller;

import com.zqq.forum.common.AppConfig;
import com.zqq.forum.common.AppResult;
import com.zqq.forum.common.ResultCode;
import com.zqq.forum.model.User;
import com.zqq.forum.service.IUserService;
import com.zqq.forum.utils.MD5Util;
import com.zqq.forum.utils.UUIDUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource(name = "userServiceImpl")
    private IUserService iUserService;

    /**
     * 用户注册
     * @return
     */
    @PostMapping("/register")
    public AppResult register(@RequestParam("username") @Nonnull String username,
                              @RequestParam("nickname") @Nonnull String nickname,
                              @RequestParam("password") @Nonnull String password,
                              @RequestParam("passwordRepeat") @Nonnull String passwordRepeat){
//        校验密码是否相同
        if(!password.equals(passwordRepeat)){
            log.warn(ResultCode.FAILED_TWO_PWD_NOT_SAME.toString());
            return AppResult.failed(ResultCode.FAILED_TWO_PWD_NOT_SAME);
        }
//        准备数据
        User user=new User();
        user.setUsername(username);
        user.setNickname(nickname);
//        处理密码
        String salt= UUIDUtil.UUID_32();
        String encryptPassword = MD5Util.md5Salt(password, salt);

        user.setPassword(encryptPassword);
        user.setSalt(salt);

        iUserService.createNormalUser(user);

        return AppResult.success();
    }

    /**
     * 用户登入
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public AppResult login(@Nonnull @RequestParam("username") String username,
                           @Nonnull @RequestParam("password")String password,
                           HttpServletRequest request){

        User user = iUserService.login(username, password);

        HttpSession session = request.getSession(true);
        session.setAttribute(AppConfig.USER_SESSION,user);

        return AppResult.success();
    }

    /**
     * 获取用户信息
     * @param request
     * @param id
     * @return
     */
    @GetMapping("/info")
    public AppResult<User> getUserInfo(HttpServletRequest request,
                                       @RequestParam(value = "id",required = false) Long id){
        User user=null;
        if(id==null){

            HttpSession session=request.getSession(false);
            user=(User) session.getAttribute(AppConfig.USER_SESSION);

        }else{
            user=iUserService.selectById(id);
        }
        if(user==null){
            return AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS);
        }

        return AppResult.success(user);
    }


    /**
     * 退出
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public AppResult logout(HttpServletRequest request){

        HttpSession session=request.getSession(false);

        if(session!=null){
            log.info("退出成功,已登入的退出");
            session.invalidate();
        }

        return AppResult.success("退出成功");
    }

}
