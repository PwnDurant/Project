package org.mon.blog_system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mon.blog_system.common.pojo.dataobject.BlogInfo;
import org.mon.blog_system.common.pojo.dataobject.UserInfo;

import java.util.List;

@Mapper
public interface BlogInfoMapper extends BaseMapper<BlogInfo> {

}
