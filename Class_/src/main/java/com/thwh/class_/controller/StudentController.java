package com.thwh.class_.controller;


import com.thwh.class_.common.base.R;
import com.thwh.class_.domain.StudentInfo;
import com.thwh.class_.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @RequestMapping("/getStudentList")
    public R<List<StudentInfo>> getStudentList(){
        return studentService.getStudentList();
    }

    @RequestMapping("/addStudent")
    public R<Boolean> addStudent(@RequestBody StudentInfo studentInfo){
        return studentService.addStudent(studentInfo);
    }

    @RequestMapping("/updateStudent")
    public R<Boolean> updateStudent(@RequestBody StudentInfo studentInfo){
        return studentService.updateStudent(studentInfo);
    }

    @RequestMapping("/deleteStudent")
    public R<Boolean> deleteStudent(Long studentId){
        return studentService.deleteStudent(studentId);
    }

}
