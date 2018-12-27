/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: TestContorller
 * Author:   cretin
 * Date:     12/25/18 17:18
 * Description: 测试Controller
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.controller;

import com.util.ServiceResult;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * 〈测试Controller〉
 *
 * @author cretin
 * @create 12/25/18
 * @since 1.0.0
 */
@RestController
@RequestMapping( "/test" )
@ResponseBody
public class TestContorller {
    @RequestMapping( value = "/email", method = RequestMethod.GET )
    public ServiceResult sendMail() {
        return ServiceResult.success("发送成功");
    }

    /***
     * 获取excel数据
     * @return 返回文件名称及excel文件的URL
     * @throws IOException
     */
    @RequestMapping( "excel" )
    public ServiceResult createExcel(HttpServletResponse response) {

        return ServiceResult.success("666");
    }


}