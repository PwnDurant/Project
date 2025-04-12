package com.zqq.eyes.service.business.file;

import com.zqq.eyes.common.pojo.OSSResult;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    OSSResult upload(MultipartFile multipartFile);
}
