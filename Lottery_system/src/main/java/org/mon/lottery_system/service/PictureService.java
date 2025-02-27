package org.mon.lottery_system.service;


import org.springframework.web.multipart.MultipartFile;

public interface PictureService {


    /**
     * 上传文件（保存图片）
     * @param file Spring MVC里面专门处理文件的一个工具类
     * @return 索引，上传后的文件名（唯一性）
     */
    String savePicture(MultipartFile file);

}
