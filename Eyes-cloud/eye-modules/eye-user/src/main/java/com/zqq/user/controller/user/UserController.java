package com.zqq.user.controller.user;


import com.zqq.common.core.controller.BaseController;
import com.zqq.common.core.domain.R;
import com.zqq.user.domain.dto.UserDTO;
import com.zqq.user.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private IUserService userService;

    /**
     * 发送验证码
     * @param userDTO
     * @return
     */
    @PostMapping("/sendCode")
    public R<Void> sendCode(@RequestBody UserDTO userDTO){
        return toR(userService.sendCode(userDTO));
    }



}
