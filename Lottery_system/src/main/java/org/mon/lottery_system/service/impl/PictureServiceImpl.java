package org.mon.lottery_system.service.impl;

import cn.hutool.core.lang.UUID;
import org.mon.lottery_system.common.errorcode.ServiceErrorCodeConstants;
import org.mon.lottery_system.common.exception.ServiceException;
import org.mon.lottery_system.service.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;


@Service
public class PictureServiceImpl implements PictureService {

    @Value("${pic.local-path}")
    private String localPath;

    @Override
    public String savePicture(MultipartFile file)  {
//        创建目录
        File dir=new File(localPath);
        if(!dir.exists()){
            dir.mkdirs(); //创建多级目录
        }
//        创建索引
//        aaa.jpj -> xxx.jpg
//        获取当前文件全名称 .jpg xxx xxx.jpg
        String filename= file.getOriginalFilename();
        assert filename!=null;
        String suffix=filename.substring(filename.lastIndexOf("."));
//        生成文件名
        filename= UUID.randomUUID()+suffix;

//        图片保存
//        将当前的file文件写入进目标文件中
        try {
            file.transferTo(new File(localPath+"/"+filename));
        }catch (IOException e){
            throw new ServiceException(ServiceErrorCodeConstants.PIC_UPLOAD_ERROR);
        }

        return filename;

    }











}
