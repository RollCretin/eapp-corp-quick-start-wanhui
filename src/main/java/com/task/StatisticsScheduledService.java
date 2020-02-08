package com.task;

import com.config.Constant;
import com.config.TokenInfoConfig;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiDepartmentGetResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.mapper.AccessTokenMapper;
import com.mapper.CommonMapper;
import com.mapper.MealSupportMapper;
import com.mapper.UserMapper;
import com.model.domain.AccessToken;
import com.model.domain.AppConfig;
import com.model.domain.MealSupport;
import com.model.domain.User;
import com.model.domain.UserManager;
import com.model.excel.ExcelData;
import com.service.MailService;
import com.util.AccessTokenUtil;
import com.util.CommRequest;
import com.util.ExcelUtils;
import com.util.StringUtils;
import com.util.TimeUtils;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.config.URLConstant.URL_GET_TOKKEN;

/**
 * 日常通知定时任务
 */
@Component
public class StatisticsScheduledService {
    @Autowired
    private MailService mailService;

    @Autowired
    private MealSupportMapper mealSupportMapper;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private AccessTokenMapper accessTokenMapper;

    @Autowired
    private UserMapper userMapper;

    @Scheduled( cron = "0 0/10 * * * ?" )
    public void getAccessToken() {
        try {
            DefaultDingTalkClient client = new DefaultDingTalkClient(URL_GET_TOKKEN);
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(Constant.APP_KEY);
            request.setAppsecret(Constant.APP_SECRET);
            request.setHttpMethod("GET");
            OapiGettokenResponse response = client.execute(request);
            String accessToken = response.getAccessToken();
            //token过期时间
            TokenInfoConfig.getInstance().setAccessToken(accessToken);
            Long expiresIn = response.getExpiresIn();
            TokenInfoConfig.getInstance().setLastExpiresTime(System.currentTimeMillis() - 60 + expiresIn / 2);

            List<AccessToken> tokenList = accessTokenMapper.getTokenList();
            if ( tokenList == null || tokenList.isEmpty() ) {
                //没有
                accessTokenMapper.insert(accessToken);
            } else {
                //有 取第一条
                AccessToken accessToken1 = tokenList.get(0);
                accessTokenMapper.update(accessToken, accessToken1.getId());
            }
            System.out.println("我更新了一次token");
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    //次月5号给相关人员发布信息
    @Scheduled( cron = "0 0 10 5 * ? " )
    public void statistics() {
        DateTime now = DateTime.now().minusMonths(1);

        String fileName = "followme_" + DateTime.now().toString("yyyyMMddHHmmsss") + ".xlsx";
        File file = new File(Constant.EXCEL_PATH);
        if ( !file.exists() && !file.isDirectory() ) {
            file.mkdirs();
        }
        File aimFile = new File(file, fileName);
        List<UserManager> allUserManager =
                commonMapper.getAllUserManager();
        if ( allUserManager == null || allUserManager.isEmpty() )
            return;
        if ( !aimFile.exists() ) {
            //获取配置
            AppConfig appConfig =
                    commonMapper.getAppConfig();
            ExcelData data = new ExcelData();
            data.setName(now.getYear() + "年" + now.getMonthOfYear() + "月餐补申请统计");
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
            List<MealSupport> allUserMealSupport = mealSupportMapper.findAllUserMealSupport(now.getYear(), now.getMonthOfYear());
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
            }
        }

        for ( UserManager userManager : allUserManager ) {
            sendEmail(mailService, userManager, now, fileName, 5);
        }
    }

    /**
     * 发送邮件
     *
     * @param userManager
     * @param now
     * @param fileName
     * @param times
     */
    public static void sendEmail(MailService mailService, UserManager userManager, DateTime now, String fileName, int times) {
        try {
            //mico@followme.cn  792075058@qq.com
            mailService.sendMail(userManager.getEmail(),
                    "万汇互联" + now.toString("yyyy年MM月") + "员工餐补统计",
                    "尊敬的管理员：" + userManager.getUserName() + ":\n你好！\n此邮件由《FM小助手》小程序自动生成，附件为"
                            + "万汇互联" + now.toString("yyyy年MM月") + "员工餐补统计Excel表，请查收。如遇文件打不开请联系我再次获取！\n"
                            + DateTime.now().toString("yyyy-MM-dd HH:mm:ss" + "\n"),
                    Constant.EXCEL_PATH + File.separatorChar + fileName);
        } catch ( Exception e ) {
            times--;
            if ( times > 0 ) {
                sendEmail(mailService, userManager, now, fileName, times);
            }
            e.printStackTrace();
        }
    }
}