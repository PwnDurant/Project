package com.zyp.note.controller;


import com.zyp.note.common.base.R;
import com.zyp.note.pojo.domain.NoteInfo;
import com.zyp.note.service.INoteService;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/note")
public class NoteController {

    @Resource(name = "noteServiceImpl")
    private INoteService noteService;

    @GetMapping("/getList")
    public R<List<NoteInfo>> getList(){
        return noteService.getList();
    }

    @PostMapping("/add")
    public R<Boolean> add(@RequestBody NoteInfo noteInfo){
        return noteService.addNote(noteInfo);
    }

    @PostMapping("/update")
    public R<Boolean> update(@RequestBody NoteInfo noteInfo){
        return noteService.updateNote(noteInfo);
    }

    @PostMapping("/delete")
    public R<Boolean> deleteNote(Long noteId){
        return noteService.deleteNote(noteId);
    }



}
