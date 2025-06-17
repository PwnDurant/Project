package com.ygt.park.controller;

import com.ygt.park.common.base.R;
import com.ygt.park.domain.CarInfo;
import com.ygt.park.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {

    @Autowired
    private CarService carService;

    @RequestMapping("/getCarList")
    public R<List<CarInfo>> getCarList(){
        return carService.getCarList();
    }

    @RequestMapping("/addCar")
    public R<Boolean> addCar(@RequestBody CarInfo carInfo){
        return carService.addCar(carInfo);
    }

    @RequestMapping("/updateCar")
    public R<Boolean> updateCar(@RequestBody CarInfo carInfo){
        return carService.updateCar(carInfo);
    }

    @RequestMapping("/deleteCar")
    public R<Boolean> deleteCar(Long carId){
        return carService.deleteCar(carId);
    }

}
