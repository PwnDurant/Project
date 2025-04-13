package com.zqq.api;


import com.zqq.common.core.constants.Constants;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(contextId = "RemoteJudgeService", value = Constants.JUDGE_SERVICE)
public interface RemoteJudgeService {

//    @PostMapping("/judge/doJudgeJavaCode")
//    R<UserQuestionResultVO> doJudgeJavaCode(@RequestBody JudgeSubmitDTO judgeSubmitDTO);
}
