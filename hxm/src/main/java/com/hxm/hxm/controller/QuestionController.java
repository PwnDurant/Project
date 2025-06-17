package com.hxm.hxm.controller;


import com.hxm.hxm.common.base.R;
import com.hxm.hxm.domain.QuestionInfo;
import com.hxm.hxm.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping("/getQuestionList")
    public R<List<QuestionInfo>> getQuestionList(){
        return questionService.getQuestionList();
    }

    @RequestMapping("/addQuestion")
    public R<Boolean> addQuestion(@RequestBody QuestionInfo questionInfo){
        return questionService.addQuestion(questionInfo);
    }

    @RequestMapping("/updateQuestion")
    public R<Boolean> updateQuestion(@RequestBody QuestionInfo questionInfo){
        return questionService.updateQuestion(questionInfo);
    }

    @RequestMapping("/deleteQuestion")
    public R<Boolean> deleteQuestion(Long questionId){
        return questionService.deleteQuestion(questionId);
    }

}
