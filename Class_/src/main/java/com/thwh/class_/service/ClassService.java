package com.thwh.class_.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thwh.class_.common.base.R;
import com.thwh.class_.common.base.ResultCode;
import com.thwh.class_.common.exception.SystemException;
import com.thwh.class_.domain.ClassInfo;
import com.thwh.class_.domain.StudentInfo;
import com.thwh.class_.mapper.ClassMapper;
import com.thwh.class_.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService {

    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private StudentMapper studentMapper;

    public R<Boolean> addClass(ClassInfo classInfo) {
        ClassInfo classInfo1 = classMapper.selectOne(new LambdaQueryWrapper<ClassInfo>()
                .eq(ClassInfo::getClassName, classInfo.getClassName())
                .eq(ClassInfo::getDeleteFlag, 0));
        if(classInfo1!=null) throw new SystemException(ResultCode.FAILED);
        int insert = classMapper.insert(classInfo);
        if(insert==1) return R.ok();
        return R.fail();
    }

    public R<List<ClassInfo>> getClassList() {
        List<ClassInfo> classInfos = classMapper.selectList(new LambdaQueryWrapper<ClassInfo>()
                .eq(ClassInfo::getDeleteFlag, 0)
                .orderByDesc(ClassInfo::getCreateTime));
        return R.ok(classInfos);
    }

    public R<List<StudentInfo>> getDetail(Long classId) {
        List<StudentInfo> studentInfos = studentMapper.selectList(new LambdaQueryWrapper<StudentInfo>()
                .eq(StudentInfo::getClassId, classId)
                .eq(StudentInfo::getDeleteFlag, 0));
        return R.ok(studentInfos);
    }

    public R<Boolean> addClassStudent(Long studentId, Long classId) {
        StudentInfo studentInfo = studentMapper.selectOne(new LambdaQueryWrapper<StudentInfo>()
                .eq(StudentInfo::getId, studentId)
                .eq(StudentInfo::getDeleteFlag, 0));
        studentInfo.setClassId(classId);
        int row = studentMapper.updateById(studentInfo);
        if(row!=1) throw new SystemException(ResultCode.FAILED);
        ClassInfo classInfo = classMapper.selectOne(new LambdaQueryWrapper<ClassInfo>()
                .eq(ClassInfo::getId, classId)
                .eq(ClassInfo::getDeleteFlag, 0));
        classInfo.setLeftNumber(classInfo.getLeftNumber()-1);
        int row1 = classMapper.updateById(classInfo);
        if(row1!=1) throw new SystemException(ResultCode.FAILED);
        return R.ok();
    }

    public R<Boolean> reduceClassStudent(Long studentId, Long classId) {
        StudentInfo studentInfo = studentMapper.selectOne(new LambdaQueryWrapper<StudentInfo>()
                .eq(StudentInfo::getId, studentId)
                .eq(StudentInfo::getDeleteFlag, 0));
        studentInfo.setClassId(0L);
        int row = studentMapper.updateById(studentInfo);
        if(row!=1) throw new SystemException(ResultCode.FAILED);
        ClassInfo classInfo = classMapper.selectOne(new LambdaQueryWrapper<ClassInfo>()
                .eq(ClassInfo::getId, classId)
                .eq(ClassInfo::getDeleteFlag, 0));
        classInfo.setLeftNumber(classInfo.getLeftNumber()+1);
        int row1 = classMapper.updateById(classInfo);
        if(row1!=1) throw new SystemException(ResultCode.FAILED);
        return R.ok();
    }

    public R<Boolean> updateClass(ClassInfo classInfo) {
        ClassInfo classInfo1 = classMapper.selectOne(new LambdaQueryWrapper<ClassInfo>()
                .eq(ClassInfo::getId, classInfo.getId())
                .eq(ClassInfo::getDeleteFlag, 0));
        if(classInfo1==null) throw new SystemException(ResultCode.FAILED);
        int row = classMapper.updateById(classInfo);
        if(row!=1) throw new SystemException(ResultCode.FAILED);
        return R.ok();
    }

    public R<Boolean> deleteClass(Long classId) {
        ClassInfo classInfo1 = classMapper.selectOne(new LambdaQueryWrapper<ClassInfo>()
                .eq(ClassInfo::getId, classId)
                .eq(ClassInfo::getDeleteFlag, 0));
        if(classInfo1==null) throw new SystemException(ResultCode.FAILED);
        classInfo1.setDeleteFlag(1);
        int row = classMapper.updateById(classInfo1);
        if(row!=1) throw new SystemException(ResultCode.FAILED);
        return R.ok();
    }
}
