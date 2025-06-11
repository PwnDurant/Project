package com.wpf.roomsimple.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wpf.roomsimple.common.base.R;
import com.wpf.roomsimple.domain.RoomInfo;
import com.wpf.roomsimple.mapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomMapper roomMapper;

    public R<List<RoomInfo>> getList() {
        List<RoomInfo> roomInfos = roomMapper.selectList(new LambdaQueryWrapper<RoomInfo>()
                .orderByDesc(RoomInfo::getCreateTime));
        return R.ok(roomInfos);
    }


    public R<Boolean> addRoom(RoomInfo roomInfo) {
        RoomInfo roomInfo1 = roomMapper.selectOne(new LambdaQueryWrapper<RoomInfo>()
                .eq(RoomInfo::getRoomName, roomInfo.getRoomName()));
        if(roomInfo1!=null) return R.fail();
        int insert = roomMapper.insert(roomInfo);
        if(insert==1) return R.ok();
        return R.fail();
    }

    public R<Boolean> updateRoom(RoomInfo roomInfo){
        RoomInfo roomInfo1 = roomMapper.selectOne(new LambdaQueryWrapper<RoomInfo>()
                .eq(RoomInfo::getId, roomInfo.getId()));
        if(roomInfo1==null) return R.fail();
        int row = roomMapper.updateById(roomInfo);
        if(row==1) return R.ok();
        return R.fail();
    }

    public R<Boolean> deleteRoom(Long roomId){
        RoomInfo roomInfo = roomMapper.selectOne(new LambdaQueryWrapper<RoomInfo>()
                .eq(RoomInfo::getId, roomId));
        if(roomInfo==null) return R.fail();
        int row = roomMapper.deleteById(roomInfo);
        if(row==1) return R.ok();
        return R.fail();
    }
}
