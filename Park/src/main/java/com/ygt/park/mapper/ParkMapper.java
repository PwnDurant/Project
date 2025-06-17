package com.ygt.park.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygt.park.domain.ParkInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ParkMapper extends BaseMapper<ParkInfo> {
}
