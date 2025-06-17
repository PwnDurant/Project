package com.hxm.hxm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hxm.hxm.common.base.R;
import com.hxm.hxm.common.base.ResultCode;
import com.hxm.hxm.common.exception.SystemException;
import com.hxm.hxm.domain.QuestionInfo;
import com.hxm.hxm.domain.TypeInfo;
import com.hxm.hxm.mapper.QuestionMapper;
import com.hxm.hxm.mapper.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {

    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private QuestionMapper questionMapper;


    public R<Boolean> addType(TypeInfo typeInfo) {
        TypeInfo typeInfo1 = typeMapper.selectOne(new LambdaQueryWrapper<TypeInfo>()
                .eq(TypeInfo::getTypeName, typeInfo.getTypeName())
                .eq(TypeInfo::getDeleteFlag, 0));
        if(typeInfo1!=null) throw new SystemException(ResultCode.FAILED);
        int insert = typeMapper.insert(typeInfo);
        if(insert==1) return R.ok();
        return R.fail();
    }

    public R<List<TypeInfo>> getTypeList() {
        List<TypeInfo> typeInfos = typeMapper.selectList(new LambdaQueryWrapper<TypeInfo>()
                .eq(TypeInfo::getDeleteFlag, 0)
                .orderByDesc(TypeInfo::getCreateTime));
        return R.ok(typeInfos);
    }

    public R<List<QuestionInfo>> getDetail(Long typeId) {
        List<QuestionInfo> questionInfos = questionMapper.selectList(new LambdaQueryWrapper<QuestionInfo>()
                .eq(QuestionInfo::getTypeId, typeId)
                .eq(QuestionInfo::getDeleteFlag, 0));
        return R.ok(questionInfos);
    }

    public R<Boolean> addTypeQuestion(Long typeId, Long questionId) {
        QuestionInfo questionInfo = questionMapper.selectOne(new LambdaQueryWrapper<QuestionInfo>()
                .eq(QuestionInfo::getId, questionId)
                .eq(QuestionInfo::getDeleteFlag, 0));
        questionInfo.setTypeId(typeId);
        int row = questionMapper.updateById(questionInfo);
        if(row!=1) throw new SystemException(ResultCode.FAILED);
        TypeInfo typeInfo = typeMapper.selectOne(new LambdaQueryWrapper<TypeInfo>()
                .eq(TypeInfo::getId, typeId)
                .eq(TypeInfo::getDeleteFlag, 0));
        typeInfo.setTotalNumber(typeInfo.getTotalNumber()+1);
        int row1 = typeMapper.updateById(typeInfo);
        if(row1!=1) throw new SystemException(ResultCode.FAILED);
        return R.ok();
    }

    public R<Boolean> reduceTypeQuestion(Long typeId, Long questionId) {
        QuestionInfo questionInfo = questionMapper.selectOne(new LambdaQueryWrapper<QuestionInfo>()
                .eq(QuestionInfo::getId, questionId)
                .eq(QuestionInfo::getDeleteFlag, 0));
        questionInfo.setTypeId(0L);
        int row = questionMapper.updateById(questionInfo);
        if(row!=1) throw new SystemException(ResultCode.FAILED);
        TypeInfo typeInfo = typeMapper.selectOne(new LambdaQueryWrapper<TypeInfo>()
                .eq(TypeInfo::getId, typeId)
                .eq(TypeInfo::getDeleteFlag, 0));
        typeInfo.setTotalNumber(typeInfo.getTotalNumber()-1);
        int row1 = typeMapper.updateById(typeInfo);
        if(row1!=1) throw new SystemException(ResultCode.FAILED);
        return R.ok();
    }

    public R<Boolean> updateType(TypeInfo typeInfo) {
        TypeInfo typeInfo1 = typeMapper.selectOne(new LambdaQueryWrapper<TypeInfo>()
                .eq(TypeInfo::getId, typeInfo.getId())
                .eq(TypeInfo::getDeleteFlag, 0));
        if(typeInfo1==null) throw new SystemException(ResultCode.FAILED);
        int row = typeMapper.updateById(typeInfo);
        if(row==1) return R.ok();
        return R.fail();
    }

    public R<Boolean> deleteType(Long typeId) {
        TypeInfo typeInfo1 = typeMapper.selectOne(new LambdaQueryWrapper<TypeInfo>()
                .eq(TypeInfo::getId, typeId)
                .eq(TypeInfo::getDeleteFlag, 0));
        if(typeInfo1==null) throw new SystemException(ResultCode.FAILED);
        typeInfo1.setDeleteFlag(1);
        int row = typeMapper.updateById(typeInfo1);
        if(row==1) return R.ok();
        return R.fail();
    }
}
