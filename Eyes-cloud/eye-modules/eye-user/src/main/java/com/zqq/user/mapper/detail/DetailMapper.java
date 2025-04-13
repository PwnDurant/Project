package com.zqq.user.mapper.detail;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zqq.user.domain.detail.RecordPredictionResult;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface DetailMapper extends BaseMapper<RecordPredictionResult> {

    void insertBatch(@Param("list") List<RecordPredictionResult> list);

}
