package com.zqq.blog_improve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zqq.blog_improve.common.base.R;
import com.zqq.blog_improve.common.base.ResultCode;
import com.zqq.blog_improve.common.exception.BlogException;
import com.zqq.blog_improve.common.exception.UserException;
import com.zqq.blog_improve.common.pojo.domain.BlogInfo;
import com.zqq.blog_improve.common.pojo.domain.UserInfo;
import com.zqq.blog_improve.common.pojo.dto.UserLoginDTO;
import com.zqq.blog_improve.common.pojo.vo.UserInfoVO;
import com.zqq.blog_improve.common.pojo.vo.UserLoginVO;
import com.zqq.blog_improve.common.utils.SecurityUtil;
import com.zqq.blog_improve.common.utils.TokenService;
import com.zqq.blog_improve.mapper.BlogMapper;
import com.zqq.blog_improve.mapper.UserMapper;
import com.zqq.blog_improve.service.IUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Resource(name = "tokenService")
    private TokenService tokenService;

    private final UserMapper userMapper;
    private final BlogMapper blogMapper;

    public UserServiceImpl(UserMapper userMapper,BlogMapper blogMapper) {
        this.userMapper = userMapper;
        this.blogMapper = blogMapper;
    }

    /**
     * 处理用户登入逻辑
     * @param userLoginDTO 登入传入的 用户名，密码
     * @return 返回登入成功的 userId 和 token 令牌
     */
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
//        判断传入账号合法性
        UserInfo selected = verify(userLoginDTO);
//        密码正确  -- 构造返回数据
        return assembleUserLoginVO(selected);
    }

    /**
     * 获得用户信息
     * @param userId 用户Id
     * @return 返回用户信息
     */
    @Override
    public R<UserInfoVO> getUserInfoById(Integer userId) {
        UserInfo userInfo= getUserInfo(userId);
        if(userInfo==null){
            log.error("用户不存在,userId:{}",userId);
            throw new UserException(ResultCode.USER_NOT_EXISTS);
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(userInfo,userInfoVO);
        return R.ok(userInfoVO);
    }

    /**
     * 获取作者信息
     * @param blogId 博客 id
     * @return 返回对应博客id 的用户信息
     */
    @Override
    public R<UserInfoVO> getAuthorInfoById(Integer blogId) {
//        根据博客id返回作者id
        BlogInfo blogInfo = blogMapper.selectOne(new LambdaQueryWrapper<BlogInfo>()
                .eq(BlogInfo::getId, blogId)
                .eq(BlogInfo::getDeleteFlag, 0));
        if(blogInfo==null){
            log.error("对应博客不存在,blogId:{}",blogId);
            throw new BlogException(ResultCode.BLOG_IS_NOT_EXIST);
        }
        Long userId=blogInfo.getUserId();
//        根据作者 ID 拿到作者详情
        UserInfo userInfo = userMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, userId)
                .eq(UserInfo::getDeleteFlag, 0));
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(userInfo,userInfoVO);
        return R.ok(userInfoVO);
    }

    /**
     * 处理用户注册请求
     * @param userLoginDTO 注册需要携带的数据：账号名和密码
     * @return 是否注册成功
     */
    @Override
    public Boolean register(UserLoginDTO userLoginDTO) {
        UserInfo userInfo = new UserInfo();
        String s = SecurityUtil.encrypt_traditional(userLoginDTO.getPassword());
        userInfo.setUserName(userLoginDTO.getUserName());
        userInfo.setPassword(s);
        int insert = userMapper.insert(userInfo);
        return insert == 1;
    }

    /**
     * 根据用户 Id 获得用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    private UserInfo getUserInfo(Integer userId) {
        return userMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, userId)
                .eq(UserInfo::getDeleteFlag, 0));
    }

    /**
     * 构造返回数据
     * @param userInfo 数据库中用户信息
     * @return 返回封装好的 VO 返回
     */
    private UserLoginVO assembleUserLoginVO(UserInfo userInfo){
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setUserId(userInfo.getId());
        userLoginVO.setToken(tokenService.createToken(userInfo));
        return userLoginVO;
    }

    /**
     * 判断账号合法性是否正确
     * @param userLoginDTO 传入的信息
     * @return 返回合法实体
     */
    private UserInfo verify(UserLoginDTO userLoginDTO) {
        //        根据用户名去查询用户信息，并判断用户是否存在
        UserInfo selected = userMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserName, userLoginDTO.getUserName())
                .eq(UserInfo::getDeleteFlag, 0));
//        用户不存在抛出异常
        if(selected==null){
            throw new UserException(ResultCode.USER_NOT_EXISTS);
        }
//        用户存在的话校验密码是否正确 不正确的话就会抛出异常
        SecurityUtil.verify_traditional(userLoginDTO.getPassword(), selected.getPassword());
        return selected;
    }


}
