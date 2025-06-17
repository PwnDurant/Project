package com.hxm.hxm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hxm.hxm.common.base.R;
import com.hxm.hxm.common.base.ResultCode;
import com.hxm.hxm.common.exception.SystemException;
import com.hxm.hxm.domain.QuestionInfo;
import com.hxm.hxm.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    public R<List<QuestionInfo>> getQuestionList() {
        List<QuestionInfo> questionInfos = questionMapper.selectList(new LambdaQueryWrapper<QuestionInfo>()
                .eq(QuestionInfo::getDeleteFlag, 0)
                .orderByDesc(QuestionInfo::getCreateTime));
        return R.ok(questionInfos);
    }

    public R<Boolean> addQuestion(QuestionInfo questionInfo) {
        QuestionInfo questionInfo1 = questionMapper.selectOne(new LambdaQueryWrapper<QuestionInfo>()
                .eq(QuestionInfo::getQuestionName, questionInfo.getQuestionName())
                .eq(QuestionInfo::getDeleteFlag, 0));
        if(questionInfo1!=null) throw new SystemException(ResultCode.FAILED);
        int insert = questionMapper.insert(questionInfo);
        if(insert==1) return R.ok();
        return R.fail();
    }

    public R<Boolean> updateQuestion(QuestionInfo questionInfo) {
        QuestionInfo questionInfo1 = questionMapper.selectOne(new LambdaQueryWrapper<QuestionInfo>()
                .eq(QuestionInfo::getId, questionInfo.getId())
                .eq(QuestionInfo::getDeleteFlag, 0));
        if(questionInfo1==null) throw new SystemException(ResultCode.FAILED);
        int row = questionMapper.updateById(questionInfo);
        if(row==1) return R.ok();
        return R.fail();
    }

    public R<Boolean> deleteQuestion(Long questionId) {
        QuestionInfo questionInfo1 = questionMapper.selectOne(new LambdaQueryWrapper<QuestionInfo>()
                .eq(QuestionInfo::getId, questionId)
                .eq(QuestionInfo::getDeleteFlag, 0));
        if(questionInfo1==null) throw new SystemException(ResultCode.FAILED);
        questionInfo1.setDeleteFlag(1);
        int row = questionMapper.updateById(questionInfo1);
        if(row==1) return R.ok();
        return R.fail();
    }
}
