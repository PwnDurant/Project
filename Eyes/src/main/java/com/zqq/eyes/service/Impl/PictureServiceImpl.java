package com.zqq.eyes.service.Impl;

import com.zqq.eyes.service.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class PictureServiceImpl implements PictureService {

    @Value("${pic.local-path}")
    private String localPath;

    @Override
    public String savePicture(MultipartFile file) {

        File dir=new File(localPath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        String filename=file.getOriginalFilename();
        assert filename!=null;
        String suffix=filename.substring(filename.lastIndexOf("."));
        filename= UUID.randomUUID()+suffix;

        try{
            file.transferTo(new File(localPath+"/"+filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filename;

    }
}
