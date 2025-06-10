package com.zyp.note.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyp.note.pojo.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {
}
