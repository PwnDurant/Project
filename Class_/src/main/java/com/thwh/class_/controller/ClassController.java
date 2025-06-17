package com.thwh.class_.controller;


import com.thwh.class_.common.base.R;
import com.thwh.class_.domain.ClassInfo;
import com.thwh.class_.domain.StudentInfo;
import com.thwh.class_.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private ClassService classService;

    /**
     * 添加班级
     * @param classInfo
     * @return
     */
    @PostMapping("/addClass")
    public R<Boolean> addClass(@RequestBody ClassInfo classInfo){
        return classService.addClass(classInfo);
    }

    /**
     * 获取班级列表
     * @return
     */
    @GetMapping("/getClassList")
    public R<List<ClassInfo>> getClassList(){
        return classService.getClassList();
    }

    /**
     * 获取班级详情
     * @param classId
     * @return
     */
    @GetMapping("/getDetail")
    public R<List<StudentInfo>> getDetail(Long classId){
        return classService.getDetail(classId);
    }

    /**
     * 给班级添加学生
     * @param studentId
     * @param classId
     * @return
     */
    @PostMapping("/addClassStudent")
    public R<Boolean> addClassStudent(Long studentId,Long classId){
        return classService.addClassStudent(studentId,classId);
    }

    /**
     * 给班级减少学生
     * @param studentId
     * @param classId
     * @return
     */
    @PostMapping("/reduceClassStudent")
    public R<Boolean> reduceClassStudent(Long studentId,Long classId){
        return classService.reduceClassStudent(studentId,classId);
    }

    /**
     * 更新班级信息
     * @param classInfo
     * @return
     */
    @PostMapping("/updateClass")
    public R<Boolean> updateClass(@RequestBody ClassInfo classInfo){
        return classService.updateClass(classInfo);
    }

    /**
     * 删除班级
     * @param classId
     * @return
     */
    @PostMapping("/deleteClass")
    public R<Boolean> deleteClass(Long classId){
        return classService.deleteClass(classId);
    }

}
