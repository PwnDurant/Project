package com.zqq.blog_improve.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zqq.blog_improve.common.base.ResultCode;
import com.zqq.blog_improve.common.constant.Constants;
import com.zqq.blog_improve.common.exception.UserException;
import com.zqq.blog_improve.common.pojo.domain.UserInfo;
import com.zqq.blog_improve.common.pojo.dto.UserLoginDTO;
import com.zqq.blog_improve.common.pojo.vo.UserLoginVO;
import com.zqq.blog_improve.common.utils.JwtUtil;
import com.zqq.blog_improve.common.utils.SecurityUtil;
import com.zqq.blog_improve.common.utils.TokenService;
import com.zqq.blog_improve.mapper.BlogMapper;
import com.zqq.blog_improve.mapper.UserMapper;
import com.zqq.blog_improve.service.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;

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
