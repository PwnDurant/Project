package com.wpf.roomsimple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wpf.roomsimple.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {



}
