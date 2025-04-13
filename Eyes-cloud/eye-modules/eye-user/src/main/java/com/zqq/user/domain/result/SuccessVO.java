package com.zqq.user.domain.result;


import com.zqq.user.domain.eye.vo.EyeVO;
import lombok.Data;

@Data
public class SuccessVO {

    private RecordResult recordResult;

    private EyeVO eyeVO;

}
