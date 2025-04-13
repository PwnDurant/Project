package com.zqq.user.manager.user;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zqq.common.core.constants.CacheConstants;
import com.zqq.common.redis.service.RedisService;
import com.zqq.user.domain.user.User;
import com.zqq.user.domain.user.vo.UserVO;
import com.zqq.user.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * 用户缓存管理器
 */
@Component
public class UserCacheManager {

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserMapper userMapper;


    public UserVO getUserById(Long userId) {
//        先获取id在缓存中对应的信息
        String userKey=getUserKey(userId);
        UserVO userVO=redisService.getCacheObject(userKey, UserVO.class);
//        如果存在的话就延长10min缓存信息然后直接返回
        if (userVO != null) {
            redisService.expire(userKey, CacheConstants.USER_EXP, TimeUnit.MINUTES);
            return userVO;
        }
//        走到这就说明缓存中不存在用户信息，所以需要再次去数据库中进行查询然后刷新
        User user=userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUserId,
                        User::getAge,
                        User::getName,
                        User::getGender,
                        User::getEmail,
                        User::getPhone)
                .eq(User::getUserId,userId));
        if(user==null){
            return null;
        }
//        刷新缓存
        refreshUser(user);
        userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }

    public void refreshUser(User user) {
        //刷新用户缓存
        String userKey = getUserKey(user.getUserId());
        redisService.setCacheObject(userKey, user);
        //设置用户缓存有效期为10分钟
        redisService.expire(userKey, CacheConstants.USER_EXP, TimeUnit.MINUTES);
    }

    private String getUserKey(Long userId) {
        return CacheConstants.USER_DETAIL + userId;
    }
}
