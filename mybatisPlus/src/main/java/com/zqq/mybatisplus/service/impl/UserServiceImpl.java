package com.zqq.mybatisplus.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zqq.mybatisplus.domain.po.User;
import com.zqq.mybatisplus.mapper.UserMapper;
import com.zqq.mybatisplus.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Override
    @Transactional
    public void deductBalance(Long id, Integer money) {
//        查询用户
        User user = getById(id);
//        校验用户状态
        if(user==null||user.getStatus()==2){
            throw new RuntimeException("用户状态逻辑");
        }
//        校验余额
        if(user.getBalance()<money){
            throw new RuntimeException("余额不足");
        }
//        扣减余额 update user set balance = balance - ?
//        baseMapper.deductBalance(id,money);
        int remainBalance= user.getBalance()-money;

        lambdaUpdate()
                .set(User::getBalance,remainBalance)
                .set(remainBalance==0,User::getStatus,2)
                .eq(User::getId,id)
                .update();
    }

    @Override
    public List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance) {
        return lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .gt(minBalance != null, User::getBalance, minBalance)
                .lt(maxBalance != null, User::getBalance, maxBalance)
                .list();
    }

}
