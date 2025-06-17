package com.hxm.hxm.controller;

import com.hxm.hxm.common.base.R;
import com.hxm.hxm.domain.QuestionInfo;
import com.hxm.hxm.domain.TypeInfo;
import com.hxm.hxm.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/type")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @PostMapping("/addType")
    public R<Boolean> addType(@RequestBody TypeInfo typeInfo){
        return typeService.addType(typeInfo);
    }

    @GetMapping("/getTypeList")
    public R<List<TypeInfo>> getTypeList(){
        return typeService.getTypeList();
    }

    @GetMapping("/getDetail")
    public R<List<QuestionInfo>> getDetail(Long typeId){
        return typeService.getDetail(typeId);
    }

    @PostMapping("/addTypeQuestion")
    public R<Boolean> addTypeType(Long typeId, Long questionId){
        return typeService.addTypeQuestion(typeId,questionId);
    }

    @PostMapping("/reduceTypeQuestion")
    public R<Boolean> reduceTypeType(Long typeId,Long questionId){
        return typeService.reduceTypeQuestion(typeId,questionId);
    }

    @PostMapping("/updateType")
    public R<Boolean> updateType(@RequestBody TypeInfo typeInfo){
        return typeService.updateType(typeInfo);
    }

    @PostMapping("/deleteType")
    public R<Boolean> deleteType(Long typeId){
        return typeService.deleteType(typeId);
    }

}
