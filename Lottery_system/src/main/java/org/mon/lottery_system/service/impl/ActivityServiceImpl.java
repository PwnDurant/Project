package org.mon.lottery_system.service.impl;

import org.mon.lottery_system.common.errorcode.ServiceErrorCodeConstants;
import org.mon.lottery_system.common.exception.ServiceException;
import org.mon.lottery_system.controller.param.CreateActivityParam;
import org.mon.lottery_system.controller.param.CreateUserByActivityParam;
import org.mon.lottery_system.dao.mapper.UserMapper;
import org.mon.lottery_system.service.ActivityService;
import org.mon.lottery_system.service.dto.CreateActivityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private UserMapper userMapper;


    /**
     * 创建活动实现类
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class) //涉及了多表，需要保证本地事务
    public CreateActivityDTO createActivity(CreateActivityParam param) {

//        校验活动信息是否正确
        checkActivityInfo(param)

//        保存活动信息到库中


//        保存活动关联的奖品信息


//        保存活动关联的人员信息


//        整合完整的活动信息，存放redis


//        构造返回



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

//        奖品id在奖品表中是否存在


//        人员数量要大于等于奖品数量
    }
}
