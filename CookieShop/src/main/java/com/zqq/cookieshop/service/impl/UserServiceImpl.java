package com.zqq.cookieshop.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zqq.cookieshop.common.base.R;
import com.zqq.cookieshop.common.base.ResultCode;
import com.zqq.cookieshop.common.exception.SystemException;
import com.zqq.cookieshop.common.utils.SecurityUtil;
import com.zqq.cookieshop.common.utils.TokenService;
import com.zqq.cookieshop.controller.user.dto.UserDTO;
import com.zqq.cookieshop.controller.user.vo.UserVO;
import com.zqq.cookieshop.domain.User;
import com.zqq.cookieshop.mapper.UserMapper;
import com.zqq.cookieshop.service.IUserService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserMapper userMapper;

    @Resource(name = "tokenService")
    private TokenService tokenService;

    @Override
    public R<UserVO> login(UserDTO userDTO) {
//        检查用户的合法性
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserName, userDTO.getUserName()));
        if(user==null) throw new SystemException(ResultCode.USER_NOT_EXISTS);
//        用户存在开始校验密码正确性
        Boolean verified = SecurityUtil.verify_traditional(userDTO.getPassword(), user.getPassword());
        if(!verified) throw new SystemException(ResultCode.NAME_OR_PASSWORD_ERROR);
//        设置并返回 JWT 令牌
        UserVO vo = new UserVO();
        vo.setUserId(user.getId());
        vo.setToken(tokenService.createToken(user));
        return R.ok(vo);
    }

    @Override
    public R<UserVO> registerAdmin(UserDTO userDTO) {
//        检查当前用户是否存在
        extracted(userDTO);
//        插入数据
        return R.ok(insert(userDTO,1));
    }

    @Override
    public R<UserVO> registerCommon(UserDTO userDTO) {
//        检查当前用户是否存在
        extracted(userDTO);
//        插入数据
        return R.ok(insert(userDTO,0));
    }

    public UserVO insert(UserDTO userDTO,Integer isAdmin){
        User userInfo = new User();
        userInfo.setIsAdmin(isAdmin);
        userInfo.setUserName(userDTO.getUserName());
        userInfo.setPassword(SecurityUtil.encrypt_traditional(userDTO.getPassword()));
        int insert = userMapper.insert(userInfo);
        if(insert!=1) throw new SystemException(ResultCode.REGISTER_FAILED);
        UserVO vo = new UserVO();
        BeanUtil.copyProperties(userInfo,vo);
        return vo;
    }

    private void extracted(UserDTO userDTO) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserName, userDTO));
        if(user!=null) throw  new SystemException(ResultCode.USER_IS_EXIST);
    }
}
