package com.wpf.roomsimple.controller;

import com.wpf.roomsimple.common.base.R;
import com.wpf.roomsimple.domain.RoomInfo;
import com.wpf.roomsimple.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;


    @GetMapping("/getRoomList")
    public R<List<RoomInfo>> getList(){
        return roomService.getList();
    }

    @PostMapping("/addRoom")
    public R<Boolean> addRoom(@RequestBody RoomInfo roomInfo){
        return roomService.addRoom(roomInfo);
    }

    @PostMapping("/updateRoom")
    public R<Boolean> updateRoom(@RequestBody RoomInfo roomInfo){
        return roomService.updateRoom(roomInfo);
    }

    @PostMapping("/deleteRoom")
    public R<Boolean> deleteRoom(Long roomId){
        return roomService.deleteRoom(roomId);
    }


}
