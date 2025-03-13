package com.zqq.eyes.service;

import org.springframework.web.multipart.MultipartFile;

public interface PictureService {

    /**
     * 上传需要保存的眼睛的照片
     * @param file
     * @return
     */
    String savePicture(MultipartFile file);

}
