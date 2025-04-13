package com.zqq.user.service.user.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zqq.common.core.constants.CacheConstants;
import com.zqq.common.core.constants.Constants;
import com.zqq.common.core.constants.HttpConstants;
import com.zqq.common.core.domain.LoginUser;
import com.zqq.common.core.domain.R;
import com.zqq.common.core.domain.vo.LoginUserVO;
import com.zqq.common.core.enums.ResultCode;
import com.zqq.common.core.utils.ThreadLocalIUtil;
import com.zqq.common.message.service.AliSmsService;
import com.zqq.common.redis.service.RedisService;
import com.zqq.common.security.exception.ServiceException;
import com.zqq.common.security.service.TokenService;
import com.zqq.user.domain.user.User;
import com.zqq.user.domain.user.dto.UserDTO;
import com.zqq.user.domain.user.dto.UserUpdateDTO;
import com.zqq.user.domain.user.vo.UserVO;
import com.zqq.user.manager.user.UserCacheManager;
import com.zqq.user.mapper.user.UserMapper;
import com.zqq.user.service.user.IUserService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements IUserService {


    @Autowired
    private AliSmsService aliSmsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCacheManager userCacheManager;

    @Value("${sms.code-expiration:5}")
    private Long phoneCodeExpiration; //短信验证码过期时间

    @Value("${sms.send-limit:10}")
    private Long sendLimit; //

    @Value("${jwt.secret}")
    private String secret;

    @Value("${sms.is-send:false}")
    private boolean isSend;

    @Value("${file.oss.downloadUrl}")
    private String downloadUrl;

    /**
     * 发送验证码
     * @param userDTO 用户手机号
     * @return 验证码是否发送成功
     */
    @Override
    public boolean sendCode(UserDTO userDTO) {
//        检查手机号是否合法
        if(!checkPhone(userDTO.getPhone())){
            throw new ServiceException(ResultCode.FAILED_USER_PHONE);
        }
//        拿到存放在redis中的验证码的剩余时间，计算出是否超过60秒
        Long expire = redisService.getExpire(getPhoneCodeKey(userDTO.getPhone()), TimeUnit.SECONDS);
        if(expire!=null&&(phoneCodeExpiration*60-expire)<60){
            throw new ServiceException(ResultCode.FAILED_FREQUENT);
        }
//        从缓存中取出缓存次数判断是否大于限制次数
        Long sendTimes = redisService.getCacheObject(getCodeTimeKey(userDTO.getPhone()), Long.class);
        if(sendTimes!=null&&sendTimes>=sendLimit){
            throw new ServiceException(ResultCode.FAILED_TIME_LIMIT);
        }
//        设置是否发送开关，如果不发送默认为123456
        String code= isSend ? RandomUtil.randomNumbers(6): Constants.DEFAULT_CODE;
//        在缓存中开始存储验证码并调用api发送验证码，验证码在redis中可以最多延长为5分钟
        redisService.setCacheObject(getPhoneCodeKey(userDTO.getPhone()),code,phoneCodeExpiration, TimeUnit.MINUTES);
        if(isSend){
            boolean sendMobileCode = aliSmsService.sendMobileCode(userDTO.getPhone(), code);
            if(!sendMobileCode){
                throw new ServiceException(ResultCode.FAILED_SEND_CODE);
            }
        }
//        对发送次数进行加一操作
        redisService.increment(getCodeTimeKey(userDTO.getPhone()));
//        设置当天请求的最大次数 24小时过后才可以再次发送请求
        if(sendTimes==null){
//            说明是当天第一次获取发起请求
            long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
//            把第一个key设置过期时间，把记录发送次数的缓存设置24小时后之后清零
            redisService.expire(getCodeTimeKey(userDTO.getPhone()),seconds,TimeUnit.SECONDS);
        }
        return true;
    }

    /**
     * 登入注册
     * @param userDTO 手机号，验证码
     * @return token
     */
    @Override
    public String codeLogin(UserDTO userDTO) {
//        判断验证码是否正确
        checkCode(userDTO);
//        根据手机号找出数据库中的用户
        User user=userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone,userDTO.getPhone()));
//        判读是否是新用户
        if(user==null){
//            开始注册
            user=new User();
            user.setPhone(userDTO.getPhone());
            user.setName(Constants.DEFAULT_NAME);
            userMapper.insert(user);
        }
//        返回登入成功的令牌
        return tokenService.createToken(user.getUserId(),secret, user.getName());
    }


    /**
     * 退出登入功能
     * @param token 取出的token
     * @return 操作成功还是失败
     */
    @Override
    public boolean logout(String token) {
//        判断token都合法性并取出token
        token = judgeToken(token);
        return tokenService.deleteLoginUser(token,secret);
    }


    /**
     * 在首页展示的信息（目前只有昵称）
     * @param token header中获取token
     * @return 返回具体信息
     */
    @Override
    public R<LoginUserVO> info(String token) {
        token=judgeToken(token);
//        取出userKey对应的对象数据并判断
        LoginUser loginUser=tokenService.getLoginUser(token,secret);
        if(loginUser==null){
            return R.fail();
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(loginUser,loginUserVO);
        return R.ok(loginUserVO);
    }

    /**
     * 展示用户详细信息
     * @return 返回用户详细信息
     */
    @Override
    public UserVO detail() {
//        通过线程变量获取userId，并通过id去缓存里查找用户是否存在
        Long userId= ThreadLocalIUtil.get(Constants.USER_ID, Long.class);
        if (userId == null) {
            throw new ServiceException(ResultCode.FAILED_USER_NOT_EXISTS);
        }
        UserVO userVO=userCacheManager.getUserById(userId);
        if (userVO == null) {
            throw new ServiceException(ResultCode.FAILED_USER_NOT_EXISTS);
        }
        return userVO;
    }

    @Override
    public int edit(UserUpdateDTO userUpdateDTO) {
//        先判断用户是否存在
        User user=isExist();
        user.setName(userUpdateDTO.getName());
        user.setGender(userUpdateDTO.getGender());
        user.setAge(userUpdateDTO.getAge());
        user.setPhone(userUpdateDTO.getPhone());
        user.setEmail(userUpdateDTO.getEmail());
//        更新用户缓存
        userCacheManager.refreshUser(user);
        tokenService.refreshLoginUser(user.getName(),ThreadLocalIUtil.get(Constants.USER_KEY, String.class));
        return userMapper.updateById(user);
    }

    @NotNull
    private User isExist() {
        Long userId = ThreadLocalIUtil.get(Constants.USER_ID, Long.class);
        if (userId == null) {
            throw new ServiceException(ResultCode.FAILED_USER_NOT_EXISTS);
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new ServiceException(ResultCode.FAILED_USER_NOT_EXISTS);
        }
        return user;
    }

    @Nullable
    private static String judgeToken(String token) {
        if (StrUtil.isNotEmpty(token) && token.startsWith(HttpConstants.PREFIX)) {
            token = token.replaceFirst(HttpConstants.PREFIX, StrUtil.EMPTY);
        }
        return token;
    }


    private static boolean checkPhone(String phone) {
        Pattern regex = Pattern.compile("^1[2|3|4|5|6|7|8|9][0-9]\\d{8}$");
        Matcher m = regex.matcher(phone);
        return m.matches();
    }

    private String getPhoneCodeKey(String phone) {
        return CacheConstants.PHONE_CODE_KEY+phone;
    }

    private String getCodeTimeKey(String phone) {
        return CacheConstants.CODE_TIME_KEY+phone;
    }

    private void checkCode(UserDTO userDTO) {
//            根据手机号拿到存储redis的验证码
        String phoneCodeKey = getPhoneCodeKey(userDTO.getPhone());
        String cacheCode = redisService.getCacheObject(phoneCodeKey, String.class);
        if(StrUtil.isEmpty(cacheCode)){
            throw new ServiceException(ResultCode.FAILED_INVALID_CODE);
        }
        if(!cacheCode.equals(userDTO.getCode())){
            throw new ServiceException(ResultCode.FAILED_ERROR_CODE);
        }
//            验证码对比成功,先删除在redis中验证码
        redisService.deleteObject(phoneCodeKey);
    }
}
