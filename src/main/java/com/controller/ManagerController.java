/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ManagerController
 * Author:   cretin
 * Date:     12/29/18 10:06
 * Description: 管理员相关的接口
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.controller;

import com.config.Constant;
import com.dingtalk.api.response.OapiDepartmentGetResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.mapper.CommonMapper;
import com.mapper.MealSupportMapper;
import com.mapper.RemindMapper;
import com.mapper.UserMapper;
import com.model.domain.AppConfig;
import com.model.domain.ManageMeal;
import com.model.domain.MealSupport;
import com.model.domain.User;
import com.model.domain.UserManager;
import com.model.excel.ExcelData;
import com.model.response.MealSupportChildResp;
import com.model.response.MealSupportResp;
import com.service.MailService;
import com.task.NotifyScheduledService;
import com.task.StatisticsScheduledService;
import com.util.AccessTokenUtil;
import com.util.CommRequest;
import com.util.ExcelUtils;
import com.util.ServiceResult;
import com.util.StringUtils;
import com.util.TimeUtils;

import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 〈管理员相关的接口〉
 *
 * @author cretin
 * @create 12/29/18
 * @since 1.0.0
 */

@RestController
@RequestMapping( "/manager" )
@ResponseBody
public class ManagerController {

    @Autowired
    private MailService mailService;


    @Autowired
    private MealSupportMapper mealSupportMapper;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取申请餐补的用户列表
     *
     * @param authCode
     * @param year
     * @param month
     * @return
     */
    @RequestMapping( value = "/user_list", method = RequestMethod.POST )
    public ServiceResult<List<ManageMeal>> user_list(@RequestParam( value = "authCode" ) String authCode,
                                                     @RequestParam( value = "year" ) int year,
                                                     @RequestParam( value = "month" ) int month) {
        List<ManageMeal> manageMealList = mealSupportMapper.getManageMealList(year, month);
        if ( manageMealList != null ) {
            AppConfig appConfig = commonMapper.getAppConfig();
            int money = 20;
            if ( appConfig != null ) {
                money = appConfig.getMealMoney();
            }
            for ( ManageMeal manageMeal : manageMealList ) {
                manageMeal.setAllMoney(money * manageMeal.getTimes());
            }
            manageMealList.sort(new Comparator<ManageMeal>() {
                @Override
                public int compare(ManageMeal o1, ManageMeal o2) {
                    return o2.getAllMoney() - o1.getAllMoney();
                }
            });
            return ServiceResult.success(manageMealList);
        } else {
            return ServiceResult.success(new ArrayList<>());
        }
    }

    /**
     * 获取单个用户的申请列表
     *
     * @param authCode
     * @param year
     * @param month
     * @param user_id
     * @return
     */
    @RequestMapping( value = "/user", method = RequestMethod.POST )
    public ServiceResult<MealSupportResp> aimUser(@RequestParam( value = "authCode" ) String authCode,
                                                  @RequestParam( value = "year" ) int year,
                                                  @RequestParam( value = "month" ) int month,
                                                  @RequestParam( value = "user_id" ) String user_id) {
        List<MealSupport> mealSupports = mealSupportMapper.findMealSupport(user_id, year, month);
        List<MealSupportChildResp> respList = new ArrayList<>();
        String allMoney = getAllMoney(respList, mealSupports);
        MealSupportResp resp = new MealSupportResp();
        resp.setList(respList);
        resp.setDate(year + "年" + TimeUtils.formatInt(month) + "月");
        resp.setAllMoney(allMoney);
        return ServiceResult.success(resp);
    }

    /**
     * 通知发送餐补申请的通知
     *
     * @param authCode
     * @param request
     * @return
     */
    @RequestMapping( value = "/notice", method = RequestMethod.POST )
    public ServiceResult aimUser(@RequestParam( value = "authCode" )
                                         String authCode, HttpServletRequest request) {
        String accessToken = AccessTokenUtil.getToken();
        String userId = AccessTokenUtil.getUserId(accessToken, request, authCode);
        int userByUserId = commonMapper.getUserByUserId(userId);
        if ( userByUserId != 0 ) {
            NotifyScheduledService.sendMealSupportMsg(userMapper, "申请餐补提醒", "请需要申请餐补的同学抓紧时间申请餐补！");
            return ServiceResult.success("已发送");
        } else {
            return ServiceResult.failure("你不是管理员");
        }
    }

    /**
     * 申请获取统计数据
     *
     * @param authCode
     * @param year
     * @param month
     * @param request
     * @return
     */
    @RequestMapping( value = "/statistics", method = RequestMethod.POST )
    public ServiceResult getStatisticsData(@RequestParam( value = "authCode" )
                                                   String authCode,
                                           @RequestParam( value = "year" )
                                                   int year,
                                           @RequestParam( value = "month" )
                                                   int month, HttpServletRequest request) {
        String accessToken = AccessTokenUtil.getToken();
        String userId = AccessTokenUtil.getUserId(accessToken, request, authCode);
        String fileName = "followme_" + DateTime.now().toString("yyyyMMddHHmmsss") + ".xlsx";
        File file = new File(Constant.EXCEL_PATH);
        if ( !file.exists() && !file.isDirectory() ) {
            file.mkdirs();
        }
        File aimFile = new File(file, fileName);
        UserManager userManager =
                commonMapper.getUserManagerByUserId(userId);
        if ( userManager == null || StringUtils.isEmpty(userId) )
            return ServiceResult.failure("您没有权限发送统计文件");
        String email = userManager.getEmail();
        if ( StringUtils.isEmpty(email) ) return ServiceResult.failure("您没有权限发送统计文件");
        if ( !aimFile.exists() ) {
            //获取配置
            AppConfig appConfig =
                    commonMapper.getAppConfig();
            ExcelData data = new ExcelData();
            data.setName(year + "年" + month + "月餐补申请统计");
            List<String> titles = new ArrayList();
            titles.add("序号");
            titles.add("加班人姓名");
            titles.add("所在部门");
            titles.add("加班日期");
            titles.add("上班时间");
            titles.add("下班时间");
            titles.add("餐费（元）");
            titles.add("总计(元）");
            titles.add("备注");
            titles.add("领款签收");
            data.setTitles(titles);

            //我们需要根据id来分组
            Map<String, List<MealSupport>> groupList = new HashMap<>();
            //获取所有本月需要申请餐补的数据
            List<MealSupport> allUserMealSupport = mealSupportMapper.findAllUserMealSupport(year, month);
            for ( MealSupport mealSupport : allUserMealSupport ) {
                if ( groupList.containsKey(mealSupport.getUserId()) ) {
                    List<MealSupport> mealSupportList = groupList.get(mealSupport.getUserId());
                    mealSupportList.add(mealSupport);
                    groupList.put(mealSupport.getUserId(), mealSupportList);
                } else {
                    List<MealSupport> mealSupportList = new ArrayList<>();
                    mealSupportList.add(mealSupport);
                    groupList.put(mealSupport.getUserId(), mealSupportList);
                }
            }
            //遍历获取部门id
            for ( Map.Entry<String, List<MealSupport>> entry : groupList.entrySet() ) {
                String key = entry.getKey();
                OapiUserGetResponse userInfo = CommRequest.getUserInfo(AccessTokenUtil.getToken(), key);
                if ( userInfo != null ) {
                    List<Long> department = userInfo.getDepartment();
                    if ( department != null && !department.isEmpty() ) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for ( Long departmentId : department ) {
                            OapiDepartmentGetResponse departmentInfo = CommRequest.getDepartmentInfo(AccessTokenUtil.getToken(), departmentId + "");
                            stringBuilder.append(departmentInfo.getName() + " ");
                        }
                        List<MealSupport> value = entry.getValue();
                        for ( MealSupport mealSupport : value ) {
                            mealSupport.setDepartmentName(stringBuilder.toString());
                            mealSupport.setUserName(userInfo.getName());
                        }
                    }
                }
            }

            int singleMoney = 20;
            if ( appConfig != null ) {
                singleMoney = appConfig.getMealMoney();
            }

            List<List<Object>> rows = new ArrayList();
            int index = 1;
            //遍历map中的值
            for ( List<MealSupport> value : groupList.values() ) {
                if ( value != null ) {
                    Collections.sort(value, new Comparator<MealSupport>() {
                        @Override
                        public int compare(MealSupport o1, MealSupport o2) {
                            return o1.getDay() - o2.getDay();
                        }
                    });
                    int money = 0;
                    for ( int i = 0; i < value.size(); i++ ) {
                        MealSupport mealSupport = value.get(i);
                        money += singleMoney;
                        List<Object> row = new ArrayList();
                        row.add(index);
                        if ( StringUtils.isEmpty(mealSupport.getUserName()) ) {
                            User userById = userMapper.findUserById(mealSupport.getUserId());
                            if ( userById != null ) {
                                mealSupport.setUserName(userById.getUserName());
                            }
                        }
                        row.add(mealSupport.getUserName());
                        if ( StringUtils.isEmpty(mealSupport.getDepartmentName()) ) {
                            row.add("已离职");
                        } else {
                            row.add(mealSupport.getDepartmentName());
                        }
                        row.add(mealSupport.getYear() + "-" + TimeUtils.formatInt(mealSupport.getMonth()) + "-" + TimeUtils.formatInt(mealSupport.getDay()));
                        row.add(mealSupport.getOnduty());
                        row.add(mealSupport.getOffduty());
                        row.add(singleMoney);
                        if ( i == value.size() - 1 )
                            row.add(money);
                        else {
                            row.add("");
                        }
                        row.add("");
                        row.add("");
                        rows.add(row);
                        index++;
                    }
                }
            }

            data.setRows(rows);

            //生成本地
            try {
                FileOutputStream out = new FileOutputStream(aimFile);
                ExcelUtils.exportExcel(data, out);
                out.close();
            } catch ( Exception e ) {
                e.printStackTrace();
                return ServiceResult.failure("请求超时，请稍后再试");
            }
        }

        StatisticsScheduledService.sendEmail(mailService, userManager, new DateTime(year, month, 1, 8, 0), fileName, 5);

        //给最高级的管理员发送数据
        if ( !userId.equals("053066514830731522") ) {
            UserManager userManagerSupport =
                    commonMapper.getUserManagerByUserId("053066514830731522");
            StatisticsScheduledService.sendEmail(mailService, userManagerSupport, new DateTime(year, month, 1, 8, 0), fileName, 5);
        }

        return ServiceResult.success("发送成功");
    }


    private String getAllMoney(List<MealSupportChildResp> list, List<MealSupport> mealSupports) {
        AppConfig appConfig = commonMapper.getAppConfig();
        int money = 0;
        for ( MealSupport mealSupport : mealSupports ) {
            MealSupportChildResp mealSupportChildResp = new MealSupportChildResp();
            BeanUtils.copyProperties(mealSupport, mealSupportChildResp);
            if ( appConfig == null ) {
                mealSupportChildResp.setMoney(20);
            } else {
                mealSupportChildResp.setMoney(appConfig.getMealMoney());
            }
            money += mealSupportChildResp.getMoney();
            list.add(mealSupportChildResp);
        }
        CommRequest.sort(list);
        return money + "";
    }
}