package com.zyp.note.controller;


import com.zyp.note.service.INoteService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/note")
public class NoteController {

    @Resource(name = "noteServiceImpl")
    private INoteService noteService;



}
