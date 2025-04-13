package com.zqq.user.service.file;

import com.zqq.common.file.domain.OSSResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IFileService {
    OSSResult upload(MultipartFile file,int type);
}
