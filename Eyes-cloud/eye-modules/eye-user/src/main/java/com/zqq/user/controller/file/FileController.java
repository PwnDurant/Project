package com.zqq.user.controller.file;

import com.zqq.common.core.controller.BaseController;
import com.zqq.common.core.domain.R;
import com.zqq.common.file.domain.OSSResult;
import com.zqq.user.domain.record.Record;
import com.zqq.user.domain.result.SuccessVO;
import com.zqq.user.service.ModelService;
import com.zqq.user.service.file.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/file")
public class FileController extends BaseController {

    @Autowired
    private IFileService fileService;

    @Autowired
    private ModelService modelService;

    /**
     * 上传图片
     * @param file 文件
     * @param type 眼睛类型:1:left,2:right
     * @return 上传结果地址
     */
    @PostMapping("/upload-left")
    public R<OSSResult> upload_left(@RequestBody MultipartFile file,int type) {
        return R.ok(fileService.upload(file,type));
    }

    /**
     * 上传图片
     * @param file 文件
     * @param type 眼睛类型:1:left,2:right
     * @return 上传结果地址
     */
    @PostMapping("/upload-right")
    public R<OSSResult> upload_right(@RequestBody MultipartFile file,int type) {
        return R.ok(fileService.upload(file,type));
    }

    /**
     * 上传图片并识别
     * @param leftImageUrl 左眼图片地址
     * @param rightImageUrl  右眼图片地址
     * @return 返回最后结果
     * @throws IOException
     */
    @PostMapping("/predict-eye")
    public R<List<Map<String, Object>>> predictEye(@RequestParam(value = "left_image_url", required = false) String leftImageUrl,
                                                   @RequestParam(value = "right_image_url", required = false) String rightImageUrl) throws IOException {
        List<Map<String, Object>> result = modelService.uploadImagesFromUrl(leftImageUrl, rightImageUrl);
        return R.ok(result);
    }

}
