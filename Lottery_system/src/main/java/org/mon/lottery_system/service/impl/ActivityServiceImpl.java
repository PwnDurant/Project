package org.mon.lottery_system.service.impl;
import lombok.extern.slf4j.Slf4j;
import org.mon.lottery_system.common.errorcode.ServiceErrorCodeConstants;
import org.mon.lottery_system.common.exception.ServiceException;
import org.mon.lottery_system.common.utils.JacksonUtil;
import org.mon.lottery_system.common.utils.RedisUtil;
import org.mon.lottery_system.controller.param.CreateActivityParam;
import org.mon.lottery_system.controller.param.CreatePrizeByActivityParam;
import org.mon.lottery_system.controller.param.CreateUserByActivityParam;
import org.mon.lottery_system.controller.param.PageParam;
import org.mon.lottery_system.dao.dataobject.ActivityDO;
import org.mon.lottery_system.dao.dataobject.ActivityPrizeDO;
import org.mon.lottery_system.dao.dataobject.ActivityUserDO;
import org.mon.lottery_system.dao.dataobject.PrizeDO;
import org.mon.lottery_system.dao.mapper.*;
import org.mon.lottery_system.service.ActivityService;
import org.mon.lottery_system.service.dto.ActivityDTO;
import org.mon.lottery_system.service.dto.ActivityDetailDTO;
import org.mon.lottery_system.service.dto.CreateActivityDTO;
import org.mon.lottery_system.service.dto.PageListDTO;
import org.mon.lottery_system.service.enums.ActivityPrizeStatusEnum;
import org.mon.lottery_system.service.enums.ActivityPrizeTiersEnum;
import org.mon.lottery_system.service.enums.ActivityStatusEnum;
import org.mon.lottery_system.service.enums.ActivityUserStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
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
    @Autowired
    private RedisUtil redisUtil;

    private final String ACTIVITY_PREFIX = "ACTIVITY_";

    private final Long ACTIVITY_OUT_TIME=60*60*24*3L;

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

        cacheActivity(detailDTO);

//        构造返回
        CreateActivityDTO createActivityDTO=new CreateActivityDTO();
        createActivityDTO.setActivityId(activityDO.getId());
        return createActivityDTO;
    }

    @Override
    public PageListDTO<ActivityDTO> findActivityList(PageParam param) {

//        获取总量
        int total=activityMapper.count();

//        获取当前页列表
        List<ActivityDO> activityDOList=activityMapper.selectActivityList(param.offset(),param.getPageSize());
        List<ActivityDTO> activityDTOList=activityDOList.stream()
                .map(activityDO -> {
                    ActivityDTO activityDTO=new ActivityDTO();
                    activityDTO.setActivityId(activityDO.getId());
                    activityDTO.setActivityName(activityDO.getActivityName());
                    activityDTO.setDescription(activityDO.getDescription());
                    activityDTO.setStatus(ActivityStatusEnum.forName(activityDO.getStatus()));
                    return activityDTO;
                }).toList();

        return new PageListDTO<>(total,activityDTOList);

    }

    @Override
    public ActivityDetailDTO getActivityDetail(Long activityId) {

        if(null==activityId){
            log.warn("查询活动详细信息的activity为空");
            return null;
        }

//        查redis
        ActivityDetailDTO detailDTO = getActivityFromCache(activityId);
        if(null != detailDTO){

            log.info("查询活动详细信息detailDTO:{}",JacksonUtil.writeValueAsString(detailDTO));
            return detailDTO;
        }


//        如果没有查到，查表

//        查活动表
        ActivityDO aDO=activityMapper.selectById(activityId);

//        活动奖品表
        List<ActivityPrizeDO> apDOList=activityPrizeMapper.selectByActivityId(activityId);
//        活动人员表
        List<ActivityUserDO> auDOList=activityUserMapper.selectByActivityId(activityId);
        log.info("audoList:{},size:{}",auDOList,auDOList.size());
//        奖品表: 奖品Id的List
        List<Long> prizeIds=apDOList.stream().map(ActivityPrizeDO::getPrizeId).toList();
        List<PrizeDO> pDOList=prizeMapper.batchSelectByIds(prizeIds);
//        整合活动详细信息存放redis
        detailDTO=convertToActivityDetailDTO(aDO,auDOList,pDOList,apDOList);
        cacheActivity(detailDTO);
//        返回
        return detailDTO;
    }

    @Override
    public void cacheActivity(Long activityId) {
        if(activityId==null){
            log.warn("要缓存的活动Id为空");
            throw new ServiceException(ServiceErrorCodeConstants.CACHE_ACTIVITY_ID_IS_EMPTY);
        }

//        查询表的数据：活动表，关联奖品表，关联人员表，奖品信息表
        ActivityDO aDO=activityMapper.selectById(activityId);
        if(null==aDO) {
            log.error("要缓存的活动Id有误");
            throw new ServiceException(ServiceErrorCodeConstants.CACHE_ACTIVITY_ID_IS_ERROR);
        }

//        活动奖品表
        List<ActivityPrizeDO> apDOList=activityPrizeMapper.selectByActivityId(activityId);
//        活动人员表
        List<ActivityUserDO> auDOList=activityUserMapper.selectByActivityId(activityId);
//        奖品表: 奖品Id的List
        List<Long> prizeIds=apDOList.stream().map(ActivityPrizeDO::getPrizeId).toList();
        List<PrizeDO> pDOList=prizeMapper.batchSelectByIds(prizeIds);
//        整合活动详细信息存放redis
        cacheActivity(convertToActivityDetailDTO(aDO,auDOList,pDOList,apDOList));
//        整合完整的活动信息并缓存
    }

    /**
     * 缓存DTO（活动信息）
     * @param detailDTO
     */
    private void cacheActivity(ActivityDetailDTO detailDTO) {

//        key: ACTIVITY_activityId
//        value: ActivityDetailDTO(json)

        if(null==detailDTO||null==detailDTO.getActivityId()) {
            log.warn("要缓存的活动信息为空！");
            return;
        }


//        如果活动表和关联表放入成功的话，缓存没有放成功的话，不需要回滚事务
//        抽奖端（Client）：查看活动信息，先在redis中获取，如果没有获取到为空，但是数据库中有数据，查到数据库中数据
//        可以再通过表数据构建出数据，存放redis中
        try{
            redisUtil.set(ACTIVITY_PREFIX+detailDTO.getActivityId()
                    , JacksonUtil.writeValueAsString(detailDTO)
                    ,ACTIVITY_OUT_TIME);
        }catch (Exception e){
            log.error("缓存活动异常,ActivityDetailDTO={}",JacksonUtil.writeValueAsString(detailDTO),e);

        }
    }


    /**
     * 根据活动Id从缓存中获取详细信息
     * @param activityId
     * @return
     */
    private ActivityDetailDTO getActivityFromCache(Long activityId){
        if(null==activityId){
            log.warn("从缓存中获取活动数据的key为空！");
            return null;
        }


        try{
            String str = redisUtil.get(ACTIVITY_PREFIX + activityId);

            if(!StringUtils.hasText(str)) {
                log.info("从缓存中获取活动数据为空！key={}",ACTIVITY_PREFIX+activityId);
                return null;
            }

            return  JacksonUtil.readValue(str, ActivityDetailDTO.class);
        }catch (Exception e){
            log.error("从缓存中获取活动信息异常,key={}",ACTIVITY_PREFIX+activityId,e);
            return null;
        }

    }


    /**
     * 根据基本的DO整合完整的活动信息ActivityDetailDTO
     * @param activityDO
     * @param activityUserDOList
     * @param prizeDOList
     * @param activityPrizeDOList
     * @return
     */

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
                prizeDTO.setName(prizeDO.getName());
                prizeDTO.setImageUrl(prizeDO.getImageUrl());
                prizeDTO.setPrice(prizeDO.getPrice());
                prizeDTO.setDescription(prizeDO.getDescription());
            });

            prizeDTO.setTier(ActivityPrizeTiersEnum.forName(apDO.getPrizeTiers()));
            prizeDTO.setPrizeAmount(apDO.getPrizeAmount());
            prizeDTO.setStatus(ActivityPrizeStatusEnum.forName(apDO.getStatus()));
            return prizeDTO;
        }).toList();

        activityDetailDTO.setPrizeDTOList(prizeDTOList);


        List<ActivityDetailDTO.UserDTO> userDTOList=activityUserDOList.stream().map(auDO->{
            ActivityDetailDTO.UserDTO userDTO=new ActivityDetailDTO.UserDTO();
            userDTO.setUserId(auDO.getUserId());
            userDTO.setUserName(auDO.getUserName());
            userDTO.setStatus(ActivityUserStatusEnum.forName(auDO.getStatus()));
            return userDTO;
        }).toList();

        activityDetailDTO.setUserDTOList(userDTOList);

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
        if(CollectionUtils.isEmpty(existUserIds)){
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_USER_ERROR);
        }
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
        if(CollectionUtils.isEmpty(existPrizeIds)){
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PRIZE_ERROR);
        }
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
