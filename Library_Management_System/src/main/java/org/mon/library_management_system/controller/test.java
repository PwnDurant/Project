package org.mon.library_management_system.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {
    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}
