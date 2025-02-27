package org.mon.lottery_system.service;

import org.mon.lottery_system.controller.param.CreatePrizeParam;
import org.mon.lottery_system.controller.param.PageParam;
import org.mon.lottery_system.service.dto.PageListDTO;
import org.mon.lottery_system.service.dto.PrizeDTO;
import org.springframework.web.multipart.MultipartFile;


public interface PrizeService {

    /**
     * 创建单个奖品
     * @param param 奖品Id
     * @param file 奖品图片
     * @return 奖品属性
     */
    Long createPrize(CreatePrizeParam param, MultipartFile file);


    /**
     * 翻页查询列表
     * @param param
     * @return
     */
    PageListDTO<PrizeDTO> findPrizeList(PageParam param);

}
