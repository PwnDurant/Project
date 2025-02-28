package org.mon.lottery_system.service.impl;


import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.jwt.JWT;
import jakarta.validation.constraints.NotBlank;
import org.mon.lottery_system.common.errorcode.ServiceErrorCodeConstants;
import org.mon.lottery_system.common.exception.ServiceException;
import org.mon.lottery_system.common.utils.JWTUtil;
import org.mon.lottery_system.common.utils.RegexUtil;
import org.mon.lottery_system.controller.param.ShortMessageLoginParam;
import org.mon.lottery_system.controller.param.UserLoginParam;
import org.mon.lottery_system.controller.param.UserPasswordLoginParam;
import org.mon.lottery_system.controller.param.UserRegisterParam;
import org.mon.lottery_system.dao.dataobject.Encrypt;
import org.mon.lottery_system.dao.dataobject.UserDO;
import org.mon.lottery_system.dao.mapper.UserMapper;
import org.mon.lottery_system.service.UserService;
import org.mon.lottery_system.service.VerificationCodeService;
import org.mon.lottery_system.service.dto.UserDTO;
import org.mon.lottery_system.service.dto.UserLoginDTO;
import org.mon.lottery_system.service.dto.UserRegisterDTO;
import org.mon.lottery_system.service.enums.UserIdentityEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * UserService的实现类
 */

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VerificationCodeService verificationCodeService;

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

    @Override
    public UserLoginDTO login(UserLoginParam userLoginParam) {
        UserLoginDTO userLoginDTO;

//        对当前的param进行类型检查，再进行类型转换，再分别操作
        if(userLoginParam instanceof UserPasswordLoginParam loginParam){
//            密码登入流程
            userLoginDTO=loginByUserPassword(loginParam);
        } else if (userLoginParam instanceof ShortMessageLoginParam loginParam) {
//            验证码登入流程
            userLoginDTO=loginByShortMessage(loginParam);
        }else {
//            不在范围内
            throw new ServiceException(ServiceErrorCodeConstants.LOGIN_INFO_NOT_EXIT);
        }

        return userLoginDTO;
    }

    @Override
    public List<UserDTO> findUserInfo(UserIdentityEnum userIdentityEnum) {

        String identityString= null==userIdentityEnum?null:userIdentityEnum.name();

//        查表
        List<UserDO> userDOList=userMapper.selectByIdentityUserLIst(identityString);

//        转换
        List<UserDTO> userDTOList=userDOList.stream().map(userDO -> {
            UserDTO userDTO=new UserDTO();
            userDTO.setUserId(userDO.getId());
            userDTO.setUserName(userDO.getUserName());
            userDTO.setEmail(userDO.getEmail());
            userDTO.setPhoneNumber(userDO.getPhoneNumber().getValue());
            userDTO.setIdentity(UserIdentityEnum.forName(userDO.getIdentity()));
            return userDTO;
        }).collect(Collectors.toList());
        return userDTOList;

    }

    @Override
    public List<UserDTO> findNormalInfo() {
        //        查表
        List<UserDO> userDOList=userMapper.selectNormalByIdentityUserLIst();

//        转换
        List<UserDTO> userDTOList=userDOList.stream().map(userDO -> {
            UserDTO userDTO=new UserDTO();
            userDTO.setUserId(userDO.getId());
            userDTO.setUserName(userDO.getUserName());
            userDTO.setEmail(userDO.getEmail());
            userDTO.setPhoneNumber(userDO.getPhoneNumber().getValue());
            userDTO.setIdentity(UserIdentityEnum.forName(userDO.getIdentity()));
            return userDTO;
        }).collect(Collectors.toList());
        return userDTOList;
    }

    /**
     * 验证码登入
     * @param loginParam
     * @return
     */
    private UserLoginDTO loginByShortMessage(ShortMessageLoginParam loginParam) {

        if(!RegexUtil.checkMobile(loginParam.getLoginMobile()))
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_ERROR);

//        获取用户数据
        UserDO userDO = userMapper.selectByPhone(new Encrypt(loginParam.getLoginMobile()));

        if(null==userDO) throw new ServiceException(ServiceErrorCodeConstants.USER_INFO_IS_EMPTY);
        else if (StringUtils.hasText(loginParam.getMandatoryIdentity())
                && !loginParam.getMandatoryIdentity().equalsIgnoreCase(userDO.getIdentity())) {
            throw new ServiceException(ServiceErrorCodeConstants.IDENTITY_ERROR);
        }

//        校验验证码
        String verificationCode = verificationCodeService.getVerificationCode(loginParam.getLoginMobile());

        if(!loginParam.getVerificationCode().equals(verificationCode))
            throw new ServiceException(ServiceErrorCodeConstants.VERIFICATION_CODE_ERROR);

        //        塞入返回值
        Map<String,Object> claim=new HashMap<>();
        claim.put("id",userDO.getId());
        claim.put("identity",userDO.getIdentity());
        String token = JWTUtil.genJwt(claim);

        UserLoginDTO userLoginDTO=new UserLoginDTO();
        userLoginDTO.setToken(token);
        userLoginDTO.setIdentity(UserIdentityEnum.forName(userDO.getIdentity()));
        return userLoginDTO;
    }

    /**
     * 密码登入
     * @param loginParam
     * @return
     */
    private UserLoginDTO loginByUserPassword(UserPasswordLoginParam loginParam) {

        UserDO userDO=new UserDO();
//        判断是手机登入还是邮箱登入
        if(RegexUtil.checkMail(loginParam.getLoginName())){
//            邮箱
//            根据邮箱查询用户表
            userDO=userMapper.selectByMail(loginParam.getLoginName());
        } else if (RegexUtil.checkMobile(loginParam.getLoginName())) {
//            手机号
//            根据手机号查询用户表
            userDO=userMapper.selectByPhone(new Encrypt(loginParam.getLoginName()));
        }else {
            throw new ServiceException(ServiceErrorCodeConstants.LOGIN_NOT_EXIT);
        }


//        校验登入信息
        if(null==userDO){
            throw new ServiceException(ServiceErrorCodeConstants.USER_INFO_IS_EMPTY);
        } else if (StringUtils.hasText(loginParam.getMandatoryIdentity())
                &&!loginParam.getMandatoryIdentity()
                .equalsIgnoreCase(userDO.getIdentity())) {
//            强制身份登入，身份校验不通过
            throw  new ServiceException(ServiceErrorCodeConstants.IDENTITY_ERROR);
        } else if (!DigestUtil.sha256Hex(loginParam.getPassword()).equals(userDO.getPassword())) {
//            校验密码不通过
            throw new ServiceException(ServiceErrorCodeConstants.PASSWORD_ERROR);
        }
//        塞入返回值
        Map<String,Object> claim=new HashMap<>();
        claim.put("id",userDO.getId());
        claim.put("identity",userDO.getIdentity());
        String token = JWTUtil.genJwt(claim);

        UserLoginDTO userLoginDTO=new UserLoginDTO();
        userLoginDTO.setToken(token);
        userLoginDTO.setIdentity(UserIdentityEnum.forName(userDO.getIdentity()));
        return userLoginDTO;
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
