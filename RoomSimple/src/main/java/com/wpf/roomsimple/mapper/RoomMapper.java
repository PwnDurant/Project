package com.wpf.roomsimple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wpf.roomsimple.domain.RoomInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomMapper extends BaseMapper<RoomInfo> {

}
