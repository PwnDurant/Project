package com.zqq.cookieshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zqq.cookieshop.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
