package org.mon.lottery_system.controller;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.mon.lottery_system.common.errorcode.ControllerErrorCodeConstants;
import org.mon.lottery_system.common.exception.ControllerException;
import org.mon.lottery_system.common.pojo.CommonResult;
import org.mon.lottery_system.common.utils.JacksonUtil;
import org.mon.lottery_system.controller.param.UserRegisterParam;
import org.mon.lottery_system.controller.result.UserRegisterResult;
import org.mon.lottery_system.service.UserService;
import org.mon.lottery_system.service.VerificationCodeService;
import org.mon.lottery_system.service.dto.UserRegisterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class UserController {

    @Resource(name = "userServiceImpl")
    private UserService userService;

    @Autowired
    private VerificationCodeService verificationCodeService;

//    private static final Logger logger= LoggerFactory.getLogger(UserController.class);
    /**
     * 注册方法
     */
    @PostMapping("/register")
    public CommonResult<UserRegisterResult>  userRegisterResultCommonResult(@Validated @RequestBody UserRegisterParam userRegisterParam){
//        日志打印
        log.info("userRegisterResultCommonResult userRegisterParam:{}",userRegisterParam);
//        转换为json形式进行打印
        log.info("userRegisterResultCommonResult userRegisterParam:{}", JacksonUtil.writeValueAsString(userRegisterParam));
//      调用Service层进行访问
        UserRegisterDTO userRegisterDTO = userService.userRegisterDTO(userRegisterParam);

        return CommonResult.success(converToUserRegisterResult(userRegisterDTO));

    }

    @RequestMapping("/verification-code/send")
    public CommonResult<Boolean> sendVerificationCode(String phoneNumber){
        log.info("sendVerificationCode phoneNumber:{}"+phoneNumber);
        verificationCodeService.sendVerificationCode(phoneNumber);
        return CommonResult.success(Boolean.TRUE);

    }

    private UserRegisterResult converToUserRegisterResult(UserRegisterDTO userRegisterDTO) {
        UserRegisterResult userRegisterResult=new UserRegisterResult();
        if(null==userRegisterDTO) throw new ControllerException(ControllerErrorCodeConstants.REGISTER_ERROR);
        userRegisterResult.setUserId(userRegisterDTO.getUserId());
        return userRegisterResult;
    }
}
