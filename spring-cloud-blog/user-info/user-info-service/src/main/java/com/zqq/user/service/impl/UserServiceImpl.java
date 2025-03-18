package com.zqq.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zqq.api.BlogServiceApi;
import com.zqq.api.pojo.BlogInfoResponse;
import com.zqq.common.constant.Constants;
import com.zqq.common.exception.BlogException;
import com.zqq.common.pojo.Result;
import com.zqq.common.utils.*;
import com.zqq.user.api.pojo.UserInfoRegisterRequest;
import com.zqq.user.api.pojo.UserInfoRequest;
import com.zqq.user.api.pojo.UserInfoResponse;
import com.zqq.user.api.pojo.UserLoginResponse;
import com.zqq.user.convert.BeanConvert;
import com.zqq.user.dataobject.UserInfo;
import com.zqq.user.mapper.UserInfoMapper;
import com.zqq.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private Mail mail;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private BlogServiceApi blogServiceApi;

    @Autowired
    private Redis redis;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final long EXPIRE_TIME=14*24*60*60;

    private static final String USER_PREFIX="user";

    @Override
    public UserLoginResponse login(UserInfoRequest user) {
        //验证账号密码是否正确
        UserInfo userInfo = queryUserInfo(user.getUserName());
        if (userInfo==null || userInfo.getId()==null){
            throw new BlogException("用户不存在");
        }
//        if (!user.getPassword().equals(userInfo.getPassword())){
//            throw new BlogException("用户密码不正确");
//        }
        if (!SecurityUtil.verify(user.getPassword(),userInfo.getPassword())){
            throw new BlogException("用户密码不正确");
        }
        //账号密码正确的逻辑
        Map<String,Object> claims = new HashMap<>();
        claims.put("id", userInfo.getId());
        claims.put("name", userInfo.getUserName());

        String jwt = JWTUtils.genJwt(claims);
        return new UserLoginResponse(userInfo.getId(), jwt);
    }


    @Override
    public UserInfoResponse getUserInfo(Integer userId) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        UserInfo userInfo = selectUserInfoById(userId);
        BeanUtils.copyProperties(userInfo, userInfoResponse);
        return userInfoResponse;
    }

    @Override
    public UserInfoResponse selectAuthorInfoByBlogId(Integer blogId) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
//        需要远程调用
        //1. 根据博客ID, 获取作者ID
        Result<BlogInfoResponse> blogDetail = blogServiceApi.getBlogDetail(blogId);

        //2. 根据作者ID, 获取作者信息
        if (blogDetail == null||blogDetail.getData()==null){
            throw new BlogException("博客不存在");
        }
        UserInfo userInfo = selectUserInfoById(blogDetail.getData().getUserId());
        BeanUtils.copyProperties(userInfo, userInfoResponse);
        return userInfoResponse;
    }

    @Override
    public Integer register(UserInfoRegisterRequest userInfoRegisterRequest) {
//        校验参数：用户名不能重复，密码，邮箱，url格式校验
        checkUserInfo(userInfoRegisterRequest);
//        用户注册，插入数据库
        UserInfo userInfo= BeanConvert.convertUserInfoByEncrypt(userInfoRegisterRequest);
        try{
            int insert = userInfoMapper.insert(userInfo);
            if(insert==1){

//                存储数据到redis中
//                如果存储失败的话会导致查询时查不到信息，就从数据库中查询
                redis.set(buildKey(userInfo.getUserName()), JsonUtil.toJson(userInfo),EXPIRE_TIME);
//                发送消息
                userInfo.setPassword("******");
                rabbitTemplate.convertAndSend(Constants.USER_EXCHANGE_NAME,"",JsonUtil.toJson(userInfo));
                return userInfo.getId();
            }else {
                throw new BlogException("插入失败");
            }
        }catch (Exception e){
            log.error("用户注册失败:,e",e);
            throw new BlogException("用户注册失败");
        }

    }



    private void checkUserInfo(UserInfoRegisterRequest param) {

//        用户名不重复，邮箱格式，url格式
        UserInfo userInfo = selectUserInfoByName(param.getUserName());
        if(userInfo!=null){
            throw new BlogException("用户名已存在");
        }
//        邮箱
//        TODO
        if(!RegexUtil.checkMail(param.getEmail())){
            throw new BlogException("邮箱格式不合法");
        }
        if(!RegexUtil.checkURL(param.getGithubUrl())){
            throw new BlogException("github格式不合法");
        }

    }

    private String buildKey(String userName) {
        return redis.buildKey(USER_PREFIX, userName);
    }

    public UserInfo selectUserInfoByName(String userName) {
        return userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserName, userName).eq(UserInfo::getDeleteFlag, 0));
    }

    private UserInfo queryUserInfo(String userName) {
//        先从redis中获取
        String key=buildKey(userName);
        boolean exists= redis.hasKey(key);
        if(exists){
//            从redis中读取数据
            String userJson = redis.get(key);
            UserInfo userInfo = JsonUtil.parseJson(userJson, UserInfo.class);
            return userInfo==null?selectUserInfoByName(userJson):userInfo;
        }else{
//            从数据库中取出数据
            UserInfo userInfo = selectUserInfoByName(userName);
//            把数据库中的数据更新到redis
            redis.set(key,JsonUtil.toJson(userInfo),EXPIRE_TIME);
            return userInfo;
        }
    }
    private UserInfo selectUserInfoById(Integer userId) {
        return userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, userId).eq(UserInfo::getDeleteFlag, 0));
    }

}
