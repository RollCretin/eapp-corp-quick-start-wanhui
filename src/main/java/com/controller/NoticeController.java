package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NoticeController {

    @RequestMapping( "/notice" )
    public String noticeDing() {
        return "notice_ding.html";
    }
}
