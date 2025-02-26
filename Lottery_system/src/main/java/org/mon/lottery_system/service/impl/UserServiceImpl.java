package org.mon.lottery_system.service.impl;


import cn.hutool.crypto.digest.DigestUtil;
import jakarta.validation.constraints.NotBlank;
import org.mon.lottery_system.common.errorcode.ServiceErrorCodeConstants;
import org.mon.lottery_system.common.exception.ServiceException;
import org.mon.lottery_system.common.utils.RegexUtil;
import org.mon.lottery_system.controller.param.UserRegisterParam;
import org.mon.lottery_system.dao.dataobject.Encrypt;
import org.mon.lottery_system.dao.dataobject.UserDO;
import org.mon.lottery_system.dao.mapper.UserMapper;
import org.mon.lottery_system.service.UserService;
import org.mon.lottery_system.service.dto.UserRegisterDTO;
import org.mon.lottery_system.service.enums.UserIdentityEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * UserService的实现类
 */

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Override
    public UserRegisterDTO userRegisterDTO(UserRegisterParam userRegisterParam) {
//        校验注册信息
        checkRegisterInfo(userRegisterParam);

//        加密私密数据（构造Dao层请求）
        UserDO userDO=new UserDO();
        userDO.setUserName(userRegisterParam.getName());
        userDO.setEmail(userRegisterParam.getMail());
        userDO.setPhoneNumber(new Encrypt(userRegisterParam.getPhoneNumber()));
        userDO.setIdentity(userRegisterParam.getIdentity());
        if(StringUtils.hasText(userRegisterParam.getPassword()))
            userDO.setPassword(DigestUtil.sha256Hex(userRegisterParam.getPassword()));

//        保存数据
        userMapper.insert(userDO);

//        构造返回
        UserRegisterDTO userRegisterDTO=new UserRegisterDTO();
        userRegisterDTO.setUserId(userDO.getId());
        return userRegisterDTO;
    }

    private void checkRegisterInfo(UserRegisterParam userRegisterParam) {
        if(null==userRegisterParam) throw new ServiceException(ServiceErrorCodeConstants.REGISTER_INFO_IS_EMPTY);

//        校验邮箱格式  xxx@xxx.xxx
            if(!RegexUtil.checkMail(userRegisterParam.getMail())){
                throw new ServiceException(ServiceErrorCodeConstants.MAIL_ERROR);
            }

//        校验手机号格式
            if(!RegexUtil.checkMobile(userRegisterParam.getPhoneNumber())){
                throw new ServiceException((ServiceErrorCodeConstants.PHONE_NUMBER_ERROR));
            }
//        校验身份信息
            if(null==UserIdentityEnum.forName(userRegisterParam.getIdentity())){
                throw new ServiceException(ServiceErrorCodeConstants.IDENTITY_ERROR);
            }

//        校验管理员密码必须填
            if(userRegisterParam.getIdentity().equalsIgnoreCase(UserIdentityEnum.ADMIN.name())
            && !StringUtils.hasText(userRegisterParam.getPassword())){
                throw new ServiceException(ServiceErrorCodeConstants.PASSWORD_IS_EMPTY);
            }

//        校验密码校验大于等于6位
            if(StringUtils.hasText(userRegisterParam.getPassword())&&!RegexUtil.checkPassword(userRegisterParam.getPassword())){
                throw new ServiceException(ServiceErrorCodeConstants.PASSWORD_ERROR);
            }

//        校验邮箱是否被使用
            if(checkMailUsed(userRegisterParam.getMail())){
                throw new ServiceException(ServiceErrorCodeConstants.MAIL_USED);
            }
//        校验手机号是否被使用
        if(checkPhoneNumber(userRegisterParam.getPhoneNumber())){
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_USED);
        }
    }

    /**
     * 校验手机号是否被使用
     * @param phoneNumber
     * @return
     */
    private boolean checkPhoneNumber(@NotBlank(message = "电话不能为空") String phoneNumber) {
        int count=userMapper.countByPhone(new Encrypt(phoneNumber));
        return count>0;
    }

    private boolean checkMailUsed(@NotBlank(message = "邮箱不能为空") String mail) {
        int count= userMapper.countByMail(mail);
        return count>0;
    }
}
