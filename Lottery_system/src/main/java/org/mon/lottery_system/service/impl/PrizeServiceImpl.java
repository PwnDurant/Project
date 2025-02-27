package org.mon.lottery_system.service.impl;


import org.mon.lottery_system.controller.param.CreatePrizeParam;
import org.mon.lottery_system.controller.param.PageParam;
import org.mon.lottery_system.dao.dataobject.PrizeDO;
import org.mon.lottery_system.dao.mapper.PrizeMapper;
import org.mon.lottery_system.service.PictureService;
import org.mon.lottery_system.service.PrizeService;
import org.mon.lottery_system.service.dto.PageListDTO;
import org.mon.lottery_system.service.dto.PrizeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Service
public class PrizeServiceImpl implements PrizeService {


    @Autowired
    private PictureService pictureService;

    @Autowired
    private PrizeMapper prizeMapper;


    @Override
    public Long createPrize(CreatePrizeParam param, MultipartFile file) {
//        上传图片
        String fileName= pictureService.savePicture(file);
//        存库
        PrizeDO prizeDO=new PrizeDO();
        prizeDO.setName(param.getPrizeName());
        prizeDO.setDescription(param.getDescription());
        prizeDO.setImageUrl(fileName);
        prizeDO.setPrice(param.getPrice());
        prizeMapper.insert(prizeDO);
        return prizeDO.getId();
    }

    @Override
    public PageListDTO<PrizeDTO> findPrizeList(PageParam param) {
//        总量
        int total=prizeMapper.count();
//        查询当前页列表
        List<PrizeDTO> prizeDTOList=new ArrayList<>();

        List<PrizeDO> prizeDOList=prizeMapper.selectPrizeList(param.offset(),param.getPageSize());
        for(PrizeDO prizeDO:prizeDOList){
            PrizeDTO prizeDTO=new PrizeDTO();
            prizeDTO.setPrizeId(prizeDO.getId());
            prizeDTO.setName(prizeDO.getName());
            prizeDTO.setDescription(prizeDO.getDescription());
            prizeDTO.setImageUrl(prizeDO.getImageUrl());
            prizeDTO.setPrice(prizeDO.getPrice());
            prizeDTOList.add(prizeDTO);
        }
        return new PageListDTO<>(total,prizeDTOList);
    }
}
