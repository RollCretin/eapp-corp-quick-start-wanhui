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

import com.mapper.CommonMapper;
import com.mapper.MealSupportMapper;
import com.mapper.RemindMapper;
import com.mapper.UserMapper;
import com.model.domain.AppConfig;
import com.model.domain.ManageMeal;
import com.model.domain.MealSupport;
import com.model.response.MealSupportChildResp;
import com.model.response.MealSupportResp;
import com.task.NotifyScheduledService;
import com.util.AccessTokenUtil;
import com.util.CommRequest;
import com.util.ServiceResult;
import com.util.TimeUtils;

import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
        resp.setDate(year + TimeUtils.formatInt(month));
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
//        String accessToken = AccessTokenUtil.getToken();
//        String userId = AccessTokenUtil.getUserId(accessToken, request, authCode);
//        int userByUserId = commonMapper.getUserByUserId(userId);
//        if ( userByUserId != 0 ) {
            NotifyScheduledService.sendMealSupportMsg(userMapper, "申请餐补提醒", "请需要申请餐补的同学抓紧时间申请餐补！");
            return ServiceResult.success("已发送");
//        } else {
//            return ServiceResult.failure("你不是管理员");
//        }
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
            if ( mealSupportChildResp.getStatus() == 1 ) {
                money += mealSupportChildResp.getMoney();
            }
            list.add(mealSupportChildResp);
        }
        return money + "";
    }
}