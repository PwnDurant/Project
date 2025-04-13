package com.zqq.user.service.user.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zqq.common.core.constants.Constants;
import com.zqq.common.core.domain.PageQueryDTO;
import com.zqq.common.core.domain.TableDataInfo;
import com.zqq.common.core.utils.ThreadLocalIUtil;
import com.zqq.user.domain.record.vo.RecordVO;
import com.zqq.user.manager.record.RecordCacheManager;
import com.zqq.user.mapper.record.RecordMapper;
import com.zqq.user.service.user.IUserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRecordServiceImpl implements IUserRecordService {

    @Autowired
    private RecordCacheManager recordCacheManager;

    @Autowired
    private RecordMapper recordMapper;


    @Override
    public List<RecordVO> list(PageQueryDTO queryDTO) {
        Long userId=ThreadLocalIUtil.get(Constants.USER_ID,Long.class);
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        return recordMapper.selectUserRecordList(queryDTO,userId);
    }

    /**
     * 查询用户历史上传数据
     * @param dto 每页数据，第几页，结果，上传时间
     * @return 返回查询结果
     */
    @Override
    public TableDataInfo redisList(PageQueryDTO dto) {
        List<RecordVO> recordVOList;
        Long userId= ThreadLocalIUtil.get(Constants.USER_ID, Long.class);
//        根据总数去判断在数据库中查询还是在redis中查询
        Long total=recordCacheManager.getListSize(userId);
        if(total==null||total<=0){
//            从数据库中查询竞赛列表
            recordVOList=listDB(dto,userId);
//            查询完之后开始刷新缓存
            recordCacheManager.refreshCache(dto,userId);
            total=new PageInfo<>(recordVOList).getTotal();
        }else{
//            直接从缓存中拿去数据
            recordVOList=recordCacheManager.getRecordVOList(dto,userId);
        }
        if(CollectionUtil.isEmpty(recordVOList)){
//            说明数据库和缓存中都没有对应的记录，直接返回空就行
            return TableDataInfo.empty();
        }
        return TableDataInfo.success(recordVOList,total);
    }

    //    根据条件从数据库中查询数据
    private List<RecordVO> listDB(PageQueryDTO dto,Long userId) {
        PageHelper.startPage(dto.getPageNum(),dto.getPageSize());
        return recordMapper.selectUserRecordList(dto,userId);
    }
}
