package com.ygt.park.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ygt.park.common.base.R;
import com.ygt.park.common.base.ResultCode;
import com.ygt.park.common.exception.SystemException;
import com.ygt.park.domain.CarInfo;
import com.ygt.park.domain.ParkInfo;
import com.ygt.park.mapper.CarMapper;
import com.ygt.park.mapper.ParkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkService {
    
    @Autowired
    private ParkMapper parkMapper;
    @Autowired
    private CarMapper carMapper;
    
    public R<Boolean> addPark(ParkInfo parkInfo) {
        ParkInfo parkInfo1 = parkMapper.selectOne(new LambdaQueryWrapper<ParkInfo>()
                .eq(ParkInfo::getParkName, parkInfo.getParkName())
                .eq(ParkInfo::getDeleteFlag, 0));
        if(parkInfo1!=null) throw new SystemException(ResultCode.FAILED);
        int insert = parkMapper.insert(parkInfo);
        if(insert==1) return R.ok();
        return R.fail();
    }

    public R<List<ParkInfo>> getParkList() {
        List<ParkInfo> parkInfos = parkMapper.selectList(new LambdaQueryWrapper<ParkInfo>()
                .eq(ParkInfo::getDeleteFlag, 0));
        return R.ok(parkInfos);
    }

    public R<Boolean> updatePark(ParkInfo parkInfo) {
        ParkInfo parkInfo1 = parkMapper.selectOne(new LambdaQueryWrapper<ParkInfo>()
                .eq(ParkInfo::getId, parkInfo.getId())
                .eq(ParkInfo::getDeleteFlag, 0));
        if(parkInfo1== null) throw new SystemException(ResultCode.FAILED);
        int row = parkMapper.updateById(parkInfo);
        if(row==1) return R.ok();
        return R.fail();
    }


    public R<Boolean> deletePark(Long parkId) {
        ParkInfo parkInfo = parkMapper.selectOne(new LambdaQueryWrapper<ParkInfo>()
                .eq(ParkInfo::getId, parkId)
                .eq(ParkInfo::getDeleteFlag, 0));
        if(parkInfo==null) throw new SystemException(ResultCode.FAILED);
        ParkInfo parkInfo1 = new ParkInfo();
        BeanUtil.copyProperties(parkInfo,parkInfo1);
        parkInfo1.setDeleteFlag(1);
        int row = parkMapper.updateById(parkInfo1);
        if(row==1) return R.ok();
        return R.fail();
    }

    public R<List<CarInfo>> getDetail(Long parkId) {
        List<CarInfo> carInfos = carMapper.selectList(new LambdaQueryWrapper<CarInfo>()
                .eq(CarInfo::getParkId, parkId)
                .eq(CarInfo::getDeleteFlag,0));
        return R.ok(carInfos);
    }

    public R<Boolean> addParkCar(Long carId,Long parkId) {
        CarInfo carInfo = carMapper.selectOne(new LambdaQueryWrapper<CarInfo>()
                .eq(CarInfo::getId, carId));
        carInfo.setParkId(parkId);
        int row = carMapper.updateById(carInfo);
        ParkInfo parkInfo = parkMapper.selectOne(new LambdaQueryWrapper<ParkInfo>()
                .eq(ParkInfo::getId, parkId)
                .eq(ParkInfo::getDeleteFlag, 0));
        parkInfo.setLeftNumber(parkInfo.getLeftNumber()-1);
        int row1 = parkMapper.updateById(parkInfo);
        if(row1==1) return R.ok();
        return R.fail();
    }

    public R<Boolean> reduceParkCar(Long carId,Long parkId) {
        CarInfo carInfo = carMapper.selectOne(new LambdaQueryWrapper<CarInfo>()
                .eq(CarInfo::getId, carId));
        carInfo.setParkId(0L);
        int row = carMapper.updateById(carInfo);
        ParkInfo parkInfo = parkMapper.selectOne(new LambdaQueryWrapper<ParkInfo>()
                .eq(ParkInfo::getId, parkId)
                .eq(ParkInfo::getDeleteFlag, 0));
        parkInfo.setLeftNumber(parkInfo.getLeftNumber()+1);
        int row1 = parkMapper.updateById(parkInfo);
        if(row==1) return R.ok();
        return R.fail();
    }
}
