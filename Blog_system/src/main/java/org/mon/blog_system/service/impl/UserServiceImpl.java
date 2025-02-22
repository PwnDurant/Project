package org.mon.blog_system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.mon.blog_system.common.exception.BlogException;
import org.mon.blog_system.common.pojo.Request.UserLoginParam;
import org.mon.blog_system.common.pojo.dataobject.BlogInfo;
import org.mon.blog_system.common.pojo.dataobject.UserInfo;
import org.mon.blog_system.common.pojo.response.UserInfoResponse;
import org.mon.blog_system.common.pojo.response.UserLoginResponse;
import org.mon.blog_system.common.utils.BeanConver;
import org.mon.blog_system.common.utils.JwtUtil;
import org.mon.blog_system.common.utils.SecurityUtil;
import org.mon.blog_system.mapper.BlogInfoMapper;
import org.mon.blog_system.mapper.UserInfoMapper;
import org.mon.blog_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource(name = "userInfoMapper")
    private UserInfoMapper userInfoMapper;
    @Resource(name = "blogInfoMapper")
    private BlogInfoMapper blogInfoMapper;

    @Override
    public UserLoginResponse login(UserLoginParam userLoginParam) {
//        判断用户是否存在
//        根据用户名去查询用户信息
        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUserName, userLoginParam.getUserName()).eq(UserInfo::getDeleteFlag, 0));

//        用户不存在返回一个空的对象
        if(userInfo==null) throw new BlogException("用户不存在");

//        用户存在,校验密码是否正确
        if(!SecurityUtil.verify(userLoginParam.getPassword(),userInfo.getPassword())) throw new BlogException("密码错误");

//        密码正确
        UserLoginResponse userLoginResponse=new UserLoginResponse();
        userLoginResponse.setUserId(userInfo.getId());
//        存放载荷
        Map<String,Object> map=new HashMap<>();
        map.put("id",userInfo.getId());
        map.put("name",userInfo.getUserName());
        userLoginResponse.setToken(JwtUtil.genToken(map));

        return userLoginResponse;


    }

    @Override
    public UserInfoResponse getUserInfoById(Integer userId) {
        UserInfo userInfo = selectUserById(userId);
        return BeanConver.trans(userInfo);
    }

    @Override
    public UserInfoResponse getAuthorInfoById(Integer blogId) {
//        根据博客Id拿到作者ID
        BlogInfo blogInfo = blogInfoMapper.selectOne(new LambdaQueryWrapper<BlogInfo>().eq(BlogInfo::getId, blogId).eq(BlogInfo::getDeleteFlag, 0));
//        根据作者Id拿到作者详情
        if(blogInfo==null) throw new BlogException("博客不存在");
        UserInfo userInfo = selectUserById(blogInfo.getUserId());
        return BeanConver.trans(userInfo);
    }

    public UserInfo selectUserById(Integer userId){
        return userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getId, userId).eq(UserInfo::getDeleteFlag, 0));
    }
}
