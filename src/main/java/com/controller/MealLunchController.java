package com.controller;

import com.mapper.MealLunchListMapper;
import com.mapper.MealLunchMapper;
import com.mapper.NoticeMapper;
import com.mapper.UserMapper;
import com.model.domain.MealLunch;
import com.model.domain.MealLunchList;
import com.model.domain.Notice;
import com.model.domain.User;
import com.model.response.sql.CustomMealLunch;
import com.model.response.MealLunchListResp;
import com.model.response.sql.CustomMealLunchList;
import com.task.NotifyScheduledService;
import com.util.AccessTokenUtil;
import com.util.ServiceResult;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping( "/lunch" )
@ResponseBody
public class MealLunchController {
    @Autowired
    private MealLunchMapper mealLunchMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MealLunchListMapper mealLunchListMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    /**
     * 获取用户首页信息
     *
     * @return
     */
    @RequestMapping( "/user/main" )
    public ServiceResult userMain(@RequestParam( value = "authCode" ) String authCode,
                                  HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        //检查lunchId
        MealLunch mealLunch = mealLunchMapper.selectNeweastMealLunch();
        String userId = AccessTokenUtil.getUserId(request, authCode);
        if ( mealLunch != null ) {
            //是合法的
            //检查这个预定是否合法
            DateTime dateTime = new DateTime(mealLunch.getYear(), mealLunch.getMonth(), mealLunch.getDay(), 0, 0, 0);
            if ( dateTime.minusDays(1).plusHours(23).isBefore(DateTime.now()) ) {
                //已经过期了
                //已失效 或者 没有可用的预约
                result.put("statue", "3");
            } else {
                result.put("title", mealLunch.getTitle());
                result.put("time", dateTime.toString("yyyy/MM/dd"));
                result.put("deadline", dateTime.minusDays(1).plusHours(23).toString("yyyy/MM/dd HH:mm:ss"));
                result.put("manager_id", mealLunch.getUserId());
                result.put("lunch_id", mealLunch.getId() + "");
                MealLunchList mealLunchList = mealLunchListMapper.selectByUserIdLunchId(userId, mealLunch.getId());
                if ( mealLunchList != null ) {
                    //有了 你已经预约过了
                    result.put("statue", "2");
                    result.put("pay_state", mealLunchList.getPayState() + "");
                } else {
                    //没有预约 可以预约
                    result.put("statue", "1");
                    result.put("pay_state", "0");
                }
            }
        } else {
            //已失效 或者 没有可用的预约
            result.put("statue", "3");
        }
        //获取新预定通知状态
        //获取开启通知的状态
        Notice notice = noticeMapper.findRemindByUserId(userId);
        if ( notice == null ) {
            result.put("notice_state", "0");
        } else {
            result.put("notice_state", notice.getStatus() + "");
        }
        return ServiceResult.success(result);
    }

    /**
     * 设置是否通知
     *
     * @param authCode
     * @param state
     * @param request
     * @return
     */
    @RequestMapping( "/user/notice" )
    public ServiceResult userChangeNotice(@RequestParam( value = "authCode" ) String authCode,
                                          @RequestParam( value = "state" ) int state,
                                          HttpServletRequest request) {
        String userId = AccessTokenUtil.getUserId(request, authCode);
        Notice notice = noticeMapper.findRemindByUserId(userId);
        if ( notice == null ) {
            //没有记录
            int insert = noticeMapper.insert(userId, state, 1);
            if ( insert != 0 ) {
                return userMain(authCode, request);
            } else {
                return ServiceResult.failure("设置失败");
            }
        } else {
            int update = noticeMapper.update(notice.getId(), state, 1);
            if ( update != 0 ) {
                return userMain(authCode, request);
            } else {
                return ServiceResult.failure("设置失败");
            }
        }
    }

    /**
     * 执行预约
     *
     * @param authCode
     * @param lunchId
     * @param request
     * @return
     */
    @RequestMapping( "/user/apply" )
    public ServiceResult userApply(@RequestParam( value = "authCode" ) String authCode,
                                   @RequestParam( value = "lunchId" ) int lunchId,
                                   HttpServletRequest request) {
        //检查lunchId
        MealLunch mealLunch = mealLunchMapper.selectNeweastMealLunch();
        if ( mealLunch != null && mealLunch.getId() == lunchId ) {
            //是合法的
            String userId = AccessTokenUtil.getUserId(request, authCode);
            MealLunchList mealLunchList = mealLunchListMapper.selectByUserIdLunchId(userId, lunchId);
            if ( mealLunchList != null ) {
                //有了 你已经预约过了
                return ServiceResult.failure("你已经预约过了");
            } else {
                //没有预约 可以预约
                int insert = mealLunchListMapper.insert(userId, mealLunch.getMonth(), mealLunch.getDay(), lunchId, 0, mealLunch.getYear());
                if ( insert != 0 )
                    return userMain(authCode, request);
                else {
                    return ServiceResult.failure("预约失败");
                }
            }
        } else {
            //已失效 或者 没有可用的预约
            return ServiceResult.failure("没有可用的午餐预约");
        }
    }

    /**
     * 取消预约
     *
     * @param authCode
     * @param lunchId
     * @param request
     * @return
     */
    @RequestMapping( "/user/cancel" )
    public ServiceResult userCancelApply(@RequestParam( value = "authCode" ) String authCode,
                                         @RequestParam( value = "lunchId" ) int lunchId,
                                         HttpServletRequest request) {
        //检查lunchId
        MealLunch mealLunch = mealLunchMapper.selectNeweastMealLunch();
        if ( mealLunch != null && mealLunch.getId() == lunchId ) {
            //是合法的
            String userId = AccessTokenUtil.getUserId(request, authCode);
            mealLunchListMapper.deleteByUserIdLunchId(userId, lunchId);
            return userMain(authCode, request);
        } else {
            //已失效 或者 没有可用的预约
            return ServiceResult.failure("所取消的预约不存在或失效");
        }
    }

    /**
     * 更新支付状态
     *
     * @param authCode
     * @param lunchId
     * @param request
     * @param payState 0 取消支付状态 1 设置已支付
     * @return
     */
    @RequestMapping( "/user/pay" )
    public ServiceResult userChangePayState(@RequestParam( value = "authCode" ) String authCode,
                                            @RequestParam( value = "lunchId" ) int lunchId, @RequestParam( value = "payState" ) int payState,
                                            HttpServletRequest request) {
        //检查lunchId
        MealLunch mealLunch = mealLunchMapper.selectNeweastMealLunch();
        if ( mealLunch != null && mealLunch.getId() == lunchId ) {
            //是合法的
            String userId = AccessTokenUtil.getUserId(request, authCode);
            MealLunchList mealLunchList = mealLunchListMapper.selectByUserIdLunchId(userId, lunchId);
            if ( mealLunchList != null ) {
                //有了 你已经预约过了
                //更新状态
                mealLunchListMapper.updatePayInfo(userId, lunchId, payState);
                return userMain(authCode, request);
            } else {
                //没有预约 可以预约
                return ServiceResult.failure("请先进行午餐预定");
            }
        } else {
            //已失效 或者 没有可用的预约
            return ServiceResult.failure("所取消的预约不存在或失效");
        }
    }

    /**
     * 获取所有的午餐预约列表
     *
     * @return
     */
    @RequestMapping( "/manager/lunch/list" )
    public ServiceResult managerLunchList() {
        List<MealLunchListResp> resps = new ArrayList<>();
        List<CustomMealLunch> mealLunches = mealLunchMapper.selectAllMealLunch();
        if ( !mealLunches.isEmpty() ) {
            for ( int i = 0; i < mealLunches.size(); i++ ) {
                CustomMealLunch mealLunch = mealLunches.get(i);
                DateTime dateTime = new DateTime(mealLunch.getYear(), mealLunch.getMonth(), mealLunch.getDay(), 0, 0, 0);
                MealLunchListResp resp = new MealLunchListResp();
                resp.setApplyNum(mealLunch.getApplyNum());
                resp.setTime(dateTime.toString("yyyy-MM-dd"));
                resp.setTitle(mealLunch.getTitle());
                resp.setManagerName(mealLunch.getUserName());
                resp.setLunchId(mealLunch.getId());
                if ( i == 0 ) {
                    //看第一条是否已经过期
                    if ( dateTime.minusDays(1).plusHours(23).isBefore(DateTime.now()) ) {
                        //已经过期了
                        resp.setState("已完结");
                    } else {
                        resp.setState("进行中");
                    }
                } else {
                    resp.setState("已完结");
                }
                resps.add(resp);
            }
        }
        return ServiceResult.success(resps);
    }

    /**
     * 获取所有的午餐预约详情列表
     *
     * @return
     */
    @RequestMapping( "/manager/lunch/details" )
    public ServiceResult managerLunchDetails(@RequestParam( value = "lunchId" ) int lunchId) {
        Map<String, Object> result = new HashMap<>();
        List<CustomMealLunchList> customMealLunchLists = mealLunchListMapper.selectAllUserByLunchId(lunchId);
        int payNum = 0;
        for ( CustomMealLunchList customMealLunchList : customMealLunchLists ) {
            customMealLunchList.setAddTimeValue(new DateTime(customMealLunchList.getAddTime()).toString("yyyy-MM-dd HH:mm:ss"));
            if ( customMealLunchList.getPayState() == 1 ) {
                payNum++;
            }
        }
        result.put("data", customMealLunchLists);
        CustomMealLunch customMealLunch = mealLunchMapper.selectMealLunchById(lunchId);
        if ( customMealLunch != null ) {
            DateTime dateTime = new DateTime(customMealLunch.getYear(), customMealLunch.getMonth(), customMealLunch.getDay(), 0, 0, 0);
            result.put("title", customMealLunch.getTitle() + " - " + dateTime.toString("yyyy/MM/dd") + "\n共" + customMealLunch.getApplyNum() + "人成功预定午餐\n" +
                    "已支付：" + (payNum) + "人，总金额：" + (payNum * 20) + "元\n" +
                    "未支付：" + (customMealLunch.getApplyNum() - payNum) + "人，总金额：" + ((customMealLunch.getApplyNum() - payNum) * 20)+"元");
        }
        return ServiceResult.success(result);
    }

    /**
     * 获取管理员首页信息
     *
     * @return
     */
    @RequestMapping( "/manager/main" )
    public ServiceResult managerMain() {
        Map<String, String> result = new HashMap<>();
        result.put("today_time", DateTime.now().toString("yyyy/MM/dd"));
        //获取最新的午餐预定信息
        MealLunch mealLunch = mealLunchMapper.selectNeweastMealLunch();
        if ( mealLunch != null ) {
            //有历史创建
            result.put("has_history", "1");
            DateTime history = new DateTime(mealLunch.getYear(), mealLunch.getMonth(), mealLunch.getDay(), 0, 0, 0);
            result.put("lunch_title", mealLunch.getTitle());
            result.put("lunch_time", history.toString("yyyy/MM/dd"));
            //如果当前时间是在午餐时间的下午两点之后 就算结束了
            if ( history.plusMinutes(14 * 60).isBefore(System.currentTimeMillis()) ) {
                //已完成
                result.put("state", "已完结");
            } else {
                result.put("state", "进行中...");
            }
        } else {
            result.put("has_history", "0");
        }
        return ServiceResult.success(result);
    }

    /**
     * 创建午餐预定
     *
     * @param authCode
     * @param title
     * @param time
     * @param request
     * @return
     */
    @RequestMapping( "/manager/create" )
    public ServiceResult create(@RequestParam( value = "authCode" ) String authCode,
                                @RequestParam( value = "title" ) String title,
                                @RequestParam( value = "time" ) String time,
                                @RequestParam( value = "notify_all" ) Boolean notify_all,
                                HttpServletRequest request) {
        String[] times = time.split("-");
        int year = Integer.parseInt(times[0]);
        int month = Integer.parseInt(times[1]);
        int day = Integer.parseInt(times[2]);
        DateTime newTime = new DateTime(year, month, day, 0, 0, 0);
        MealLunch mealLunch = mealLunchMapper.selectNeweastMealLunch();
        if ( mealLunch != null ) {
            //有历史创建
            DateTime history = new DateTime(mealLunch.getYear(), mealLunch.getMonth(), mealLunch.getDay(), 0, 0, 0);
            //判断下当前时间是否是在上次历史预定时间的10点之后 10点之前不能创建下一次
            if ( history.plusMinutes(10 * 60).isAfter(System.currentTimeMillis()) ) {
                return ServiceResult.failure("存在未完成的午餐预定，请在上一次午餐预定完成之后再重新创建");
            }

            //判断下用户预定的时间点是否合理 比如历史上以及有2019-12-20的了 就不能再创建这一天或者这一天之前的了呢
            if ( newTime.isBefore(history) || newTime.isEqual(history) ) {
                return ServiceResult.failure("午餐预定时间有误，日期必须在" + newTime.toString("yyyy-MM-dd") + "之后");
            }
        }
        String userId = AccessTokenUtil.getUserId(request, authCode);
        User user = userMapper.findUserById(userId);
        if ( user != null ) {
            //满足条件
            int insert = mealLunchMapper.insert(userId, month, day, user.getUserName(), title, year);
            if ( insert != 0 ) {
                //插入成功
                //如果需要通知所有人 则发送对应的回执
                if ( notify_all ) {
                    List<Notice> allReminds = noticeMapper.findAllReminds(1);
                    for ( Notice allRemind : allReminds ) {
                        NotifyScheduledService.sendCommonMsg("新消息：刚刚发布了新的午餐预定", allRemind.getUserId(), title, user.getUserName() + "发布了" + newTime.toString("yyyy-MM-dd") + "的午餐预定");
                    }
                }
                return ServiceResult.success("创建成功");
            }
        }
        return ServiceResult.failure("创建失败，请稍后再试");
    }
}
