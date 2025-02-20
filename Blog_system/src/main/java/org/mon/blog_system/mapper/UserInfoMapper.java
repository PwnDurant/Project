package org.mon.blog_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mon.blog_system.common.pojo.dataobject.UserInfo;



@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}
