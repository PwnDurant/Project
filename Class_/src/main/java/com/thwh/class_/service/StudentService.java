package com.thwh.class_.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thwh.class_.common.base.R;
import com.thwh.class_.common.base.ResultCode;
import com.thwh.class_.common.exception.SystemException;
import com.thwh.class_.domain.StudentInfo;
import com.thwh.class_.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    public R<List<StudentInfo>> getStudentList() {
        List<StudentInfo> studentInfos = studentMapper.selectList(new LambdaQueryWrapper<StudentInfo>()
                .eq(StudentInfo::getDeleteFlag, 0)
                .orderByDesc(StudentInfo::getCreateTime));
        return R.ok(studentInfos);
    }

    public R<Boolean> addStudent(StudentInfo studentInfo) {
        StudentInfo studentInfo1 = studentMapper.selectOne(new LambdaQueryWrapper<StudentInfo>()
                .eq(StudentInfo::getStudentName, studentInfo.getStudentName())
                .eq(StudentInfo::getDeleteFlag, 0));
        if(studentInfo1!=null) throw new SystemException(ResultCode.FAILED);
        int insert = studentMapper.insert(studentInfo);
        if(insert==1) return R.ok();
        return R.fail();
    }

    public R<Boolean> updateStudent(StudentInfo studentInfo) {
        StudentInfo studentInfo1 = studentMapper.selectOne(new LambdaQueryWrapper<StudentInfo>()
                .eq(StudentInfo::getId, studentInfo.getId())
                .eq(StudentInfo::getDeleteFlag, 0));
        if(studentInfo1==null) throw new SystemException(ResultCode.FAILED);
        int row = studentMapper.updateById(studentInfo);
        if(row==1) return R.ok();
        return R.fail();
    }

    public R<Boolean> deleteStudent(Long studentId) {
        StudentInfo studentInfo1 = studentMapper.selectOne(new LambdaQueryWrapper<StudentInfo>()
                .eq(StudentInfo::getId, studentId)
                .eq(StudentInfo::getDeleteFlag, 0));
        if(studentInfo1==null) throw new SystemException(ResultCode.FAILED);
        studentInfo1.setDeleteFlag(1);
        int row = studentMapper.updateById(studentInfo1);
        if(row==1) return R.ok();
        return R.fail();
    }
}
