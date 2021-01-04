package com;

import com.config.Constant;
import com.model.DateInfoModel;
import com.service.MailService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RunWith( SpringRunner.class )
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * {
     * "year": "201912",
     * "month": [
     * {
     * "day": "02"
     * },
     * {
     * "day": "03"
     * },
     * {
     * "day": "04"
     * },
     * {
     * "day": "05"
     * },
     * {
     * "day": "06"
     * },
     * {
     * "day": "09"
     * },
     * {
     * "day": "10"
     * },
     * {
     * "day": "11"
     * },
     * {
     * "day": "12"
     * },
     * {
     * "day": "13"
     * },
     * {
     * "day": "16"
     * },
     * {
     * "day": "17"
     * },
     * {
     * "day": "18"
     * },
     * {
     * "day": "19"
     * },
     * {
     * "day": "20"
     * },
     * {
     * "day": "23"
     * },
     * {
     * "day": "24"
     * },
     * {
     * "day": "25"
     * },
     * {
     * "day": "26"
     * },
     * {
     * "day": "27"
     * },
     * {
     * "day": "30"
     * },
     * {
     * "day": "31"
     * }
     * ]
     * }
     * ]
     */
    @Test
    public void contextLoads() {
        List<List<String>> map = new ArrayList<>();
        DateInfoModel dateInfoModel = restTemplate.getForEntity("https://www.mxnzp.com/api/holiday/list/year/2021?app_id=nldmuckoppsvvtzv&app_secret=bTZ6QmhXa3ZxRmpIZTRNYTBjUlpPdz09", DateInfoModel.class).getBody();
        if ( dateInfoModel != null ) {
            for ( DateInfoModel.DataBean dataBean : dateInfoModel.getData() ) {
                //取年份 月份
                List<DateInfoModel.DataBean.DaysBean> days = dataBean.getDays();
                List<String> dayList = new ArrayList<>();
                for ( DateInfoModel.DataBean.DaysBean day : days ) {
                    dayList.add(day.getDate().substring(day.getDate().length() - 2));
                }
                map.add(dayList);
            }
        }
        for ( int i = 0; i < map.size(); i++ ) {
            System.out.println("{");
            System.out.println("\"year\":\"" + (202100 + (i + 1)) + "\",");
            System.out.println("\"month\":[");
            List<String> strings = map.get(i);
            for ( int i1 = 0; i1 < strings.size(); i1++ ) {
                System.out.println("{");
//                "day": "26"
                System.out.println("\"day\":"+"\""+strings.get(i1)+"\"");
                System.out.println("}");
                if(i1!=strings.size()-1){
                    //不是最后一个
                    System.out.println(",");
                }
            }
            System.out.println("]");
            System.out.println("},");
        }
    }

    @Autowired
    private MailService mailService;

    @Test
    public void sendEmail() {
        mailService.sendMail("mxnzp_life@163.com","滴滴滴","sssss", Constant.EXCEL_PATH + File.separatorChar + "followme_201911201454011.xlsx");
    }
}
