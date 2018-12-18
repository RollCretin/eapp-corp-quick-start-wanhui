package com.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date: on 12/7/18
 * @author: cretin
 * @email: mxnzp_life@163.com
 * @desc: 补卡申请
 */
@RestController
@RequestMapping("/repair")
public class RepairController {

    /**
     * 获取可申请补卡的日期列表
     * @return
     */
    @RequestMapping( "enable_list" )
    public String enable_list() {
        
        return "notice_ding.html";
    }
}
