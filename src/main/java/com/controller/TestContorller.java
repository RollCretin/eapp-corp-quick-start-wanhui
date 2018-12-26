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

import com.config.Constant;
import com.dingtalk.api.response.OapiDepartmentGetResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.mapper.MealSupportMapper;
import com.model.domain.MealSupport;
import com.model.excel.ExcelData;
import com.model.response.MealSupportResp;
import com.service.MailService;
import com.util.AccessTokenUtil;
import com.util.CommRequest;
import com.util.ExcelUtils;
import com.util.ServiceResult;
import com.util.TimeUtils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
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