package com.zqq.user.domain.result;


import com.zqq.user.domain.detail.vo.ResultVO;
import com.zqq.user.domain.record.vo.RecordVO;
import lombok.Data;

import java.util.List;

@Data
public class SuccessVO {

    private RecordVO recordVO;

    private List<ResultVO> resultVOList;

}
