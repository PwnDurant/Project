package com.zqq.user.controller.user;


import com.zqq.common.core.constants.HttpConstants;
import com.zqq.common.core.controller.BaseController;
import com.zqq.common.core.domain.R;
import com.zqq.common.core.domain.vo.LoginUserVO;
import com.zqq.user.domain.user.dto.UserDTO;
import com.zqq.user.domain.user.dto.UserUpdateDTO;
import com.zqq.user.domain.user.vo.UserVO;
import com.zqq.user.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 登入注册
     * @param userDTO 传入手机号和验证码
     * @return 如果登入成功返回token
     */
    @PostMapping("/code/login")
    public R<String> codeLogin(@RequestBody UserDTO userDTO){
        return R.ok(userService.codeLogin(userDTO));
    }

    /**
     * 退出登入功能
     * @param token 从header中拿出用户token
     * @return 返回操作成功还是失败
     */
    @DeleteMapping("/logout")
    public R<Void> logout(@RequestHeader(HttpConstants.AUTHENTICATION) String token){
        return toR(userService.logout(token));
    }

    /**
     * 展示用户信息
     * @param token header中的token
     * @return token中userKey对应的数据
     */
    @GetMapping("/info")
    public R<LoginUserVO> info(@RequestHeader(HttpConstants.AUTHENTICATION) String token){
        return userService.info(token);
    }

    /**
     * 展示用户具体信息
     * @return 返回用户具体信息
     */
    @GetMapping("/detail")
    public R<UserVO> detail() {
        return R.ok(userService.detail());
    }

    /**
     * 编辑用户信息
     * @param userUpdateDTO 用户所需编辑信心
     * @return 返回是否编辑成功，根据修改的行数来决定
     */
    @PutMapping("/edit")
    public R<Void> edit(@RequestBody UserUpdateDTO userUpdateDTO) {
        return toR(userService.edit(userUpdateDTO));
    }



}
