package com.zqq.eyes.controller;


import com.zqq.eyes.service.PictureService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/picture")
public class PictureController {

    @Resource(name = "pictureServiceImpl")
    private PictureService pictureService;

    @RequestMapping("/upload")
    public String uploadPic(MultipartFile file){
        return pictureService.savePicture(file);
    }


}
