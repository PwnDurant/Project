package com.ygt.park.controller;

import com.ygt.park.common.base.R;
import com.ygt.park.domain.CarInfo;
import com.ygt.park.domain.ParkInfo;
import com.ygt.park.service.ParkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/park")
public class ParkController {

    @Autowired
    private ParkService parkService;

    @PostMapping("/addPark")
    public R<Boolean> addPark(@RequestBody ParkInfo parkInfo){
        return parkService.addPark(parkInfo);
    }

    @GetMapping("/getParkList")
    public R<List<ParkInfo>> getParkList(){
        return parkService.getParkList();
    }

    @GetMapping("/getDetail")
    public R<List<CarInfo>> getDetail(Long parkId){
        return parkService.getDetail(parkId);
    }

    @PostMapping("/addParkCar")
    public R<Boolean> addParkCar(Long carId,Long parkId){
        return parkService.addParkCar(carId,parkId);
    }

    @PostMapping("/reduceParkCar")
    public R<Boolean> reduceParkCar(Long carId,Long parkId){
        return parkService.reduceParkCar(carId,parkId);
    }

    @PostMapping("/updatePark")
    public R<Boolean> updatePark(@RequestBody ParkInfo parkInfo){
        return parkService.updatePark(parkInfo);
    }

    @PostMapping("/deletePark")
    public R<Boolean> deletePark(Long parkId){
        return parkService.deletePark(parkId);
    }

}
