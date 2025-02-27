package org.mon.lottery_system.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.mon.lottery_system.common.errorcode.ControllerErrorCodeConstants;
import org.mon.lottery_system.common.exception.ControllerException;
import org.mon.lottery_system.common.pojo.CommonResult;
import org.mon.lottery_system.common.utils.JacksonUtil;
import org.mon.lottery_system.controller.param.CreatePrizeParam;
import org.mon.lottery_system.controller.param.PageParam;
import org.mon.lottery_system.controller.result.FindPrizeListResult;
import org.mon.lottery_system.service.PictureService;
import org.mon.lottery_system.service.PrizeService;
import org.mon.lottery_system.service.dto.PageListDTO;
import org.mon.lottery_system.service.dto.PrizeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;


@Slf4j
@RestController
public class PrizeController {

    @Autowired
    private PictureService pictureService;

    @Autowired
    private PrizeService prizeService;

    @RequestMapping("/pic/upload")
    public String uploadPic(MultipartFile file){
        return pictureService.savePicture(file);
    }


    /**
     * 创建奖品
     * @RequestPart :用于接收表单数据 multipart/form-data  而@RequestBody 是接收JSON对象的注解 application/json
     * @param param 这里是用表单数据进行传输的，需要加上@RequestPart注解
     * @param picFile
     * @return
     */
    @RequestMapping("/prize/create")
    public CommonResult<Long> createPrize(@Valid @RequestPart("param") CreatePrizeParam param,@RequestPart("prizePic") MultipartFile picFile){

        log.info("createPrize param1:{}",param);
        log.info("createPrize param2:{}", JacksonUtil.writeValueAsString(param));

        return CommonResult.success(prizeService.createPrize(param,picFile));
    }


    @RequestMapping("/prize/find-list")
    public CommonResult<FindPrizeListResult> findPrizeList(PageParam param){
        log.info("findPrizeList param:{}",JacksonUtil.writeValueAsString(param));

        PageListDTO<PrizeDTO> pageListDTO=prizeService.findPrizeList(param);
        return CommonResult.success(converToFindPrizeListResult(pageListDTO));
    }

    private FindPrizeListResult converToFindPrizeListResult(PageListDTO<PrizeDTO> pageListDTO) {
        if(null==pageListDTO) throw new ControllerException(ControllerErrorCodeConstants.FIND_PRIZE_LIST_ERROR);

        FindPrizeListResult findPrizeListResult=new FindPrizeListResult();
        findPrizeListResult.setTotal(pageListDTO.getTotal());
        findPrizeListResult.setRecords(pageListDTO.getRecords().stream().map(prizeDTO -> {
            FindPrizeListResult.PrizeInfo prizeInfo=new FindPrizeListResult.PrizeInfo();
            prizeInfo.setPrizeName(prizeDTO.getName());
            prizeInfo.setPrizeId(prizeDTO.getPrizeId());
            prizeInfo.setDescription(prizeDTO.getDescription());
            prizeInfo.setImageUrl(prizeDTO.getImageUrl());
            prizeInfo.setPrice(prizeDTO.getPrice());
            return prizeInfo;
        }).collect(Collectors.toList()));

        return findPrizeListResult;
    }

}
