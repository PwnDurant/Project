package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    // 微信服务接口地址
    public static final String WX_LOGIN="https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    /**
     * 微信登入
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {

        String openid = getOpenid(userLoginDTO);

//        判断当前用户是否为新用户
        User user = userMapper.getByOpenId(openid);
        if(user==null){
//        如果是应该自动完成注册
            user=User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

//        返回用户对象
        return user;
    }

    /**
     * 调用接口服务获取微信用户的openid
     * @param userLoginDTO
     * @return
     */
    private String getOpenid(UserLoginDTO userLoginDTO) {
        //        调用微信接口服务，获得当前微信用户的openId
        Map<String,String> map = new HashMap();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", userLoginDTO.getCode());
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

//        判断openId是否为空，如果为空则表示登入失败，抛出异常
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        if(openid ==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        return openid;
    }


}
