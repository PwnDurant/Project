package org.mon.lottery_system.service.impl;

import org.mon.lottery_system.common.errorcode.ServiceErrorCodeConstants;
import org.mon.lottery_system.common.exception.ServiceException;
import org.mon.lottery_system.common.utils.*;
import org.mon.lottery_system.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

//    对于redis里面的key，需要标准化
    private static final String VERIFICATION_CODE_PREFIX="VERIFICATION_CODE_";
    private static final Long VERIFICATION_CODE_TIMEOUT=60L;
    private static final String VERIFICATION_CODE_TEMPLATE_CODE="SMS_479100562";

    @Autowired
    private SMSUtil smsUtil;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public void sendVerificationCode(String phoneNumber) {
//        校验手机号
        if(!RegexUtil.checkMobile(phoneNumber)){
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_ERROR);
        }
//        生成随机验证码
        String code= CaptchaUtil.getCaptcha(4);
//        发送验证码
        Map<String,String> map=new HashMap<>();
        map.put("code",code);
        smsUtil.sendMessage(VERIFICATION_CODE_TEMPLATE_CODE,phoneNumber, JacksonUtil.writeValueAsString(map));
//        缓存验证码
        redisUtil.set(VERIFICATION_CODE_PREFIX+phoneNumber,code,VERIFICATION_CODE_TIMEOUT);

    }

    @Override
    public String getVerificationCode(String phoneNumber) {
        //        校验手机号
        if(!RegexUtil.checkMobile(phoneNumber)){
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_ERROR);
        }

        return redisUtil.get(VERIFICATION_CODE_PREFIX+phoneNumber);


    }
}
