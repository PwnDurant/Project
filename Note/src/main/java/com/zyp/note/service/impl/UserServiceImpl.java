package com.zyp.note.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyp.note.common.base.R;
import com.zyp.note.common.base.ResultCode;
import com.zyp.note.common.exception.SystemException;
import com.zyp.note.mapper.NoteMapper;
import com.zyp.note.mapper.UserMapper;
import com.zyp.note.pojo.domain.UserInfo;
import com.zyp.note.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NoteMapper noteMapper;

    @Override
    public R<Boolean> login(UserInfo userInfo) {
        UserInfo userInfo1 = userMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserName, userInfo.getUserName())
                .eq(UserInfo::getDeleteFlag, 0));
        if(userInfo1==null){
            throw new SystemException(ResultCode.FAILED);
        }
        if(userInfo.getPassword().equals(userInfo1.getPassword())){
            return R.ok();
        }
        return R.fail();
    }

    @Override
    public Boolean register(UserInfo userInfo) {
        UserInfo userInfo1 = userMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserName, userInfo.getUserName())
                .eq(UserInfo::getDeleteFlag, 0));
        if(userInfo1!=null){
            throw new SystemException(ResultCode.FAILED);
        }
        int insert = userMapper.insert(userInfo);
        return insert==1;
    }
}
