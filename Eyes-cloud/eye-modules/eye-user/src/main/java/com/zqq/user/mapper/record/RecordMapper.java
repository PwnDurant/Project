package com.zqq.user.mapper.record;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zqq.common.core.domain.PageQueryDTO;
import com.zqq.user.domain.record.Record;
import com.zqq.user.domain.record.vo.RecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RecordMapper extends BaseMapper<Record> {

//    根据用户id去数据库中查询对应的记录
    List<RecordVO> selectUserRecordList(@Param("query") PageQueryDTO dto, @Param("userId") Long userId);

}
