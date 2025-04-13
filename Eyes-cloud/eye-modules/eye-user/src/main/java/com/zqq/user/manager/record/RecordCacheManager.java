package com.zqq.user.manager.record;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.zqq.common.core.constants.CacheConstants;
import com.zqq.common.core.domain.PageQueryDTO;
import com.zqq.common.redis.service.RedisService;
import com.zqq.user.domain.record.vo.RecordVO;
import com.zqq.user.mapper.record.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RecordCacheManager {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RecordMapper recordMapper;

    /**
     * 通过用户id查询缓存中的记录总数
     * @param userId 用户id
     * @return 返回查询出来的记录总数
     */
    public Long getListSize(Long userId) {
        String userMsgListKey=getUserRecordListKey(userId);
        return redisService.getListSize(userMsgListKey);
    }

    private String getUserRecordListKey(Long userId){
        return CacheConstants.USER_RECORD_LIST+userId;
    }

    public void refreshCache(PageQueryDTO dto,Long userId) {
//        拿到当前用户的记录信息
        List<RecordVO> recordVOList=recordMapper.selectUserRecordList(dto,userId);
        if(CollectionUtil.isEmpty(recordVOList)){
            return ;
        }
//        拿到记录列表
        List<Long> recordIdList=recordVOList.stream().map(RecordVO::getRecordId).toList();
//        根据用户key插入到redis缓存中
        String userRecordListKey=getUserRecordListKey(userId);
        redisService.rightPushAll(userRecordListKey,recordIdList);
//        根据recordId再单独的存放每一条对应的信息
        Map<String ,RecordVO> recordVOMap=new HashMap<>();
        for(RecordVO recordVO:recordVOList){
            recordVOMap.put(getRecordDetailKey(recordVO.getRecordId()),recordVO);
        }
        redisService.multiSet(recordVOMap);
    }

    private String getRecordDetailKey(Long textId){
        return CacheConstants.RECORD_DETAIL+textId;
    }


    /**
     * 直接在缓存中获取数据
     * @param dto 查询字段
     * @param userId 用户id
     * @return 查询的信息
     */
    public List<RecordVO> getRecordVOList(PageQueryDTO dto, Long userId) {
        int start=(dto.getPageNum()-1)*dto.getPageSize();
        int end=start+dto.getPageSize()-1; //下标需要-1
//        获取所有缓存的记录id
        String userRecordListKey=getUserRecordListKey(userId);
        List<Long> recordIdList=redisService.getCacheListByRange(userRecordListKey,start,end, Long.class);
//        组装记录VO列表
        List<RecordVO> recordVOList=assembleRecordVOList(recordIdList);
        if(CollectionUtil.isEmpty(recordVOList)){
//            说明redis中数据有问题，从数据库中重新查询并刷新
            recordVOList=getRecordVOListByDB(dto,userId);
            refreshCache(dto,userId);
        }
        return recordVOList;
    }

    /**
     * 通过数据库查询，查询完之后再进行刷新
     * @param dto 查询信息
     * @param userId 用户id
     * @return 返回查询信息
     */
    private List<RecordVO> getRecordVOListByDB(PageQueryDTO dto, Long userId) {
        PageHelper.startPage(dto.getPageNum(),dto.getPageSize());
        return recordMapper.selectUserRecordList(dto,userId);
    }

    private List<RecordVO> assembleRecordVOList(List<Long> recordIdList) {

        if(CollectionUtil.isEmpty(recordIdList)){
            return null; //说明redis中没有数据，从数据库中查询
        }
//        拼接redis当中key的方法，并且将拼接好的key存储到一个list中
        List<String> detailKeyList=new ArrayList<>();
//        获取每一个具体的recordId
        for(Long recordId: recordIdList){
            detailKeyList.add(getRecordDetailKey(recordId));
        }
//        通过一个array集中获取所有的具体的信息
        List<RecordVO> recordVOList=redisService.multiGet(detailKeyList, RecordVO.class);
        CollUtil.removeNull(recordVOList);
        if(CollectionUtil.isEmpty(recordVOList)||recordVOList.size()!=recordIdList.size()){
//            说明redis中数据有问题 从数据库中查询数据并且重新刷新缓存
            return null;
        }
        return recordVOList;
    }
}
