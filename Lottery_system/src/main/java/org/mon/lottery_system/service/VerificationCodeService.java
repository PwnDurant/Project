package org.mon.lottery_system.service;



public interface VerificationCodeService {

    /**
     * 发送验证码
     * @param phoneNumber
     */
    void sendVerificationCode(String phoneNumber);


    /**
     * 从缓存中获取验证码
     * @param phoneNumber
     * @return
     */
    String getVerificationCode(String phoneNumber);
}
