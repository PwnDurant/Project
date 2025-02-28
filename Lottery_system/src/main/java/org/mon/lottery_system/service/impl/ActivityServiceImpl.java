package org.mon.lottery_system.service.impl;

import org.mon.lottery_system.common.errorcode.ServiceErrorCodeConstants;
import org.mon.lottery_system.common.exception.ServiceException;
import org.mon.lottery_system.controller.param.CreateActivityParam;
import org.mon.lottery_system.controller.param.CreatePrizeByActivityParam;
import org.mon.lottery_system.controller.param.CreateUserByActivityParam;
import org.mon.lottery_system.dao.dataobject.ActivityDO;
import org.mon.lottery_system.dao.dataobject.ActivityPrizeDO;
import org.mon.lottery_system.dao.dataobject.ActivityUserDO;
import org.mon.lottery_system.dao.dataobject.PrizeDO;
import org.mon.lottery_system.dao.mapper.*;
import org.mon.lottery_system.service.ActivityService;
import org.mon.lottery_system.service.dto.ActivityDetailDTO;
import org.mon.lottery_system.service.dto.CreateActivityDTO;
import org.mon.lottery_system.service.dto.PrizeDTO;
import org.mon.lottery_system.service.enums.ActivityPrizeStatusEnum;
import org.mon.lottery_system.service.enums.ActivityPrizeTiersEnum;
import org.mon.lottery_system.service.enums.ActivityStatusEnum;
import org.mon.lottery_system.service.enums.ActivityUserStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PrizeMapper prizeMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivityUserMapper activityUserMapper;

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;


    /**
     * 创建活动实现类
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class) //涉及了多表，需要保证本地事务
    public CreateActivityDTO createActivity(CreateActivityParam param) {

//        校验活动信息是否正确
        checkActivityInfo(param);

//        保存活动信息到库中
        ActivityDO activityDO=new ActivityDO();
        activityDO.setActivityName(param.getActivityName());
        activityDO.setDescription(param.getDescription());
        activityDO.setStatus(ActivityStatusEnum.RUNNING.name());
        activityMapper.insert(activityDO);


//        保存活动关联的奖品信息
        List<CreatePrizeByActivityParam> prizeParams = param.getActivityPrizeList();
        List<ActivityPrizeDO> activityPrizeDOList=prizeParams.stream()
                .map(prizeParam->{
                    ActivityPrizeDO activityPrizeDO=new ActivityPrizeDO();
                    activityPrizeDO.setActivityId(activityDO.getId());
                    activityPrizeDO.setPrizeId(prizeParam.getPrizeId());
                    activityPrizeDO.setPrizeAmount(prizeParam.getPrizeAmount());
                    activityPrizeDO.setPrizeTiers(prizeParam.getPrizeTiers());
                    activityPrizeDO.setStatus(ActivityPrizeStatusEnum.INIT.name());
                    return activityPrizeDO;
                }).collect(Collectors.toList());

        activityPrizeMapper.batchInsert(activityPrizeDOList);


//        保存活动关联的人员信息
        List<CreateUserByActivityParam> userParams = param.getActivityUserList();
        List<ActivityUserDO> activityUserDOList=userParams.stream()
                .map(userParam->{
                    ActivityUserDO activityUserDO=new ActivityUserDO();
                    activityUserDO.setActivityId(activityDO.getId());
                    activityUserDO.setUserId(userParam.getUserId());
                    activityUserDO.setUserName(userParam.getUserName());
                    activityUserDO.setStatus(ActivityUserStatusEnum.INIT.name());
                    return activityUserDO;
                }).collect(Collectors.toList());

        activityUserMapper.batchInsert(activityUserDOList);


//        整合完整的活动信息，存放redis
//        activityId:ActivityDetailDTO :活动+奖品+人员信息

//        需要先获取奖品基本属性列表
//        获取需要查询的奖品Id
        List<Long> prizeIds=param.getActivityPrizeList().stream().map(CreatePrizeByActivityParam::getPrizeId).distinct()
                .toList();
        List<PrizeDO> prizeDOList = prizeMapper.batchSelectByIds(prizeIds);

        ActivityDetailDTO detailDTO=convertToActivityDetailDTO(activityDO,activityUserDOList,prizeDOList,activityPrizeDOList);



//        构造返回



    }

    private ActivityDetailDTO convertToActivityDetailDTO(ActivityDO activityDO, List<ActivityUserDO> activityUserDOList, List<PrizeDO> prizeDOList, List<ActivityPrizeDO> activityPrizeDOList) {
        ActivityDetailDTO activityDetailDTO=new ActivityDetailDTO();
        activityDetailDTO.setActivityId(activityDO.getId());
        activityDetailDTO.setActivityName(activityDO.getActivityName());
        activityDetailDTO.setDesc(activityDO.getDescription());
        activityDetailDTO.setStatus(ActivityStatusEnum.forName(activityDO.getStatus()));

//        apDO：{prizeId amount status},{prizeId amount status},{prizeId amount status}...
//        pDO: {prizeId,name...},{prizeId,name...},{prizeId,name...}...
        List<ActivityDetailDTO.PrizeDTO> prizeDTOList=activityPrizeDOList.stream().map(apDO -> {
            ActivityDetailDTO.PrizeDTO prizeDTO=new ActivityDetailDTO.PrizeDTO();
            prizeDTO.setPrizeId(apDO.getPrizeId());
            Optional<PrizeDO> optionalPrizeDO = prizeDOList.stream()
                    .filter(prizeDO -> prizeDO.getId().equals(apDO.getPrizeId()))
                    .findFirst();


//            拿到Optional的容器对象，如果有一个prizeDO为空，那么就不会把prizeDO传入进去，不执行当前方法
            optionalPrizeDO.ifPresent(prizeDO -> {
                prizeDTO.setName(prizeDTO.getName());
                prizeDTO.setImageUrl(prizeDTO.getImageUrl());
                prizeDTO.setPrice(prizeDO.getPrice());
                prizeDTO.setDescription(prizeDTO.getDescription());
            });


            prizeDTO.setTier(ActivityPrizeTiersEnum.forName(apDO.getPrizeTiers()));
            prizeDTO.setPrizeAmount(apDO.getPrizeAmount());
            prizeDTO.setStatus(ActivityPrizeStatusEnum.forName(apDO.getStatus()));
            return prizeDTO;
        }).toList();

        activityDetailDTO.setPrizeDTOList(prizeDTOList);



        activityDetailDTO.setUserDTOList();


        return activityDetailDTO;
    }

    /**
     * 校验活动有效性
     * @param param
     */
    private void checkActivityInfo(CreateActivityParam param) {
        if(null==param) throw new ServiceException(ServiceErrorCodeConstants.CREATE_ACTIVITY_INFO_INFO_IS_EMPTY);

//        人员id在人员表中是否存在
        List<Long> userIds=param.getActivityUserList().stream().map(CreateUserByActivityParam::getUserId)
                .distinct()
                .collect(Collectors.toList());

        List<Long> existUserIds=userMapper.selectExistByIds(userIds);
        userIds.forEach(id->{
            if(!existUserIds.contains(id)){
                throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_USER_ERROR);
            }
        });

//        奖品id在奖品表中是否存在
        List<Long> prizeIds=param.getActivityPrizeList()
                .stream()
                .map(CreatePrizeByActivityParam::getPrizeId)
                .distinct()
                .collect(Collectors.toList());

        List<Long> existPrizeIds=prizeMapper.selectExistByIds(prizeIds);
        prizeIds.forEach(id->{
            if(!existPrizeIds.contains(id)){
                throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PRIZE_ERROR);
            }
        });
//        人员数量要大于等于奖品数量

        int userAmount=param.getActivityUserList().size();
        long prizeAmount=param.getActivityPrizeList()
                .stream()
                .mapToLong(CreatePrizeByActivityParam::getPrizeAmount)
                .sum();
        if(userAmount<prizeAmount) throw new ServiceException(ServiceErrorCodeConstants.USER_PRIZE_AMOUNT_ERROR);

//        校验活动奖品等级有效性
        param.getActivityPrizeList().forEach(prize->{
            if(null== ActivityPrizeTiersEnum.forName(prize.getPrizeTiers())){
                throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PRIZE_TIERS_ERROR);
            }
        });
    }
}
