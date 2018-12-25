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

import com.mapper.MealSupportMapper;
import com.model.domain.MealSupport;
import com.model.response.MealSupportResp;
import com.service.MailService;
import com.util.ServiceResult;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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
    @Autowired
    private MailService mailService;

    @Autowired
    private MealSupportMapper mealSupportMapper;

    @RequestMapping( value = "/email", method = RequestMethod.GET )
    public ServiceResult sendMail() {
        try {
            mailService.sendSimpleMail("1745980954@qq.com", "测试", "哈哈");
        } catch ( Exception e ) {
            System.out.println(e);
        }
        return ServiceResult.success("发送成功");
    }

    /***
     * 创建表头
     * @param workbook
     * @param sheet
     */
    private void createTitle(HSSFWorkbook workbook, HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(2, 12 * 256);
        sheet.setColumnWidth(3, 17 * 256);

        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("序号");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("金额");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("描述");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("日期");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("日期");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("日期");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("日期");
        cell.setCellStyle(style);
    }

    /***
     * 获取excel数据
     * @return 返回文件名称及excel文件的URL
     * @throws IOException
     */
    @SuppressWarnings( {"unchecked", "rawtypes"} )
    @RequestMapping( "getExcel" )
    public ServiceResult getExcel() throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("统计表");
        createTitle(workbook, sheet);
        DateTime now = DateTime.now();
        List<MealSupport> mealSupport = ( List<MealSupport> ) mealSupportMapper.findAllUserMealSupport(now.getYear(), now.getMonthOfYear());

        //设置日期格式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));

        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        for ( MealSupport statisticsInfo : mealSupport ) {
            HSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(rowNum);
            row.createCell(1).setCellValue(statisticsInfo.getYear());
            row.createCell(2).setCellValue(statisticsInfo.getMonth());
            row.createCell(3).setCellValue(statisticsInfo.getDay());
            row.createCell(4).setCellValue(statisticsInfo.getOnduty());
            row.createCell(5).setCellValue(statisticsInfo.getOffduty());
            row.createCell(6).setCellValue(statisticsInfo.getUserId());
            rowNum++;
        }

        //拼装blobName
        String fileName = "测试数据统计表.xlsx";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateTime = dateFormat.format(new Date());
        String blobName = "/Users/cretin/Desktop/" + fileName;
        FileOutputStream fos = new FileOutputStream(blobName);
        workbook.write(fos);
        fos.flush();
        fos.close();

        return ServiceResult.success("666");
    }
}