package com.ygt.park.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ygt.park.common.base.R;
import com.ygt.park.common.base.ResultCode;
import com.ygt.park.common.exception.SystemException;
import com.ygt.park.domain.CarInfo;
import com.ygt.park.mapper.CarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarMapper carMapper;


    public R<List<CarInfo>> getCarList() {
        List<CarInfo> carInfos = carMapper.selectList(new LambdaQueryWrapper<CarInfo>()
                .eq(CarInfo::getDeleteFlag, 0));
        return R.ok(carInfos);
    }

    public R<Boolean> addCar(CarInfo carInfo){
        CarInfo carInfo1 = carMapper.selectOne(new LambdaQueryWrapper<CarInfo>()
                .eq(CarInfo::getId, carInfo.getId())
                .eq(CarInfo::getDeleteFlag, 0));
        if(carInfo1!=null) throw new SystemException(ResultCode.FAILED);
        int insert = carMapper.insert(carInfo);
        if(insert==1) return R.ok();
        return R.fail();
    }

    public R<Boolean> updateCar(CarInfo carInfo){
        CarInfo carInfo1 = carMapper.selectOne(new LambdaQueryWrapper<CarInfo>()
                .eq(CarInfo::getId, carInfo.getId())
                .eq(CarInfo::getDeleteFlag, 0));
        if(carInfo1==null) throw new SystemException(ResultCode.FAILED);
        int row = carMapper.updateById(carInfo);
        if(row==1) return R.ok();
        return R.fail();
    }

    public R<Boolean> deleteCar(Long carId){
        CarInfo carInfo1 = carMapper.selectOne(new LambdaQueryWrapper<CarInfo>()
                .eq(CarInfo::getId, carId)
                .eq(CarInfo::getDeleteFlag, 0));
        if(carInfo1==null) throw new SystemException(ResultCode.FAILED);
        carInfo1.setDeleteFlag(1);
        int row = carMapper.updateById(carInfo1);
        if(row==1) return R.ok();
        return R.fail();
    }
}
