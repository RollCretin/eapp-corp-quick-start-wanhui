package com.util;

import com.config.Constant;
import com.config.TokenInfoConfig;
import com.config.URLConstant;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.taobao.api.ApiException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.config.URLConstant.URL_GET_TOKKEN;

/**
 * 获取access_token工具类
 */
public class AccessTokenUtil {
    private static final Logger bizLogger = LoggerFactory.getLogger(AccessTokenUtil.class);

    public static String getToken() throws RuntimeException {
        if ( TokenInfoConfig.getInstance().getLastExpiresTime() == 0 || TokenInfoConfig.getInstance().getLastExpiresTime() >= System.currentTimeMillis() ) {
            try {
                DefaultDingTalkClient client = new DefaultDingTalkClient(URL_GET_TOKKEN);
                OapiGettokenRequest request = new OapiGettokenRequest();
                request.setAppkey(Constant.APP_KEY);
                request.setAppsecret(Constant.APP_SECRET);
                request.setHttpMethod("GET");
                OapiGettokenResponse response = client.execute(request);
                String accessToken = response.getAccessToken();
                //token过期时间
                Long expiresIn = response.getExpiresIn();
                TokenInfoConfig.getInstance().setAccessToken(accessToken);
                TokenInfoConfig.getInstance().setLastExpiresTime(System.currentTimeMillis() - 60 + expiresIn);
                return accessToken;
            } catch ( ApiException e ) {
                bizLogger.error("getAccessToken failed", e);
                throw new RuntimeException();
            }
        } else {
            return TokenInfoConfig.getInstance().getAccessToken();
        }
    }

    public static String getUserId(String accessToken, HttpServletRequest servletRequest, String requestAuthCode) {//获取accessToken,注意正是代码要有异常流处理
        String userId = "";
        HttpSession session = servletRequest.getSession();
        if ( session.getAttribute("userId") == null ) {
            //获取用户信息
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_GET_USER_INFO);
            OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
            request.setCode(requestAuthCode);
            request.setHttpMethod("GET");
            OapiUserGetuserinfoResponse response;
            try {
                response = client.execute(request, accessToken);
            } catch ( ApiException e ) {
                e.printStackTrace();
                return null;
            }
            //3.查询得到当前用户的userId
            // 获得到userId之后应用应该处理应用自身的登录会话管理（session）,避免后续的业务交互（前端到应用服务端）每次都要重新获取用户身份，提升用户体验
            userId = response.getUserid();
            session.setAttribute("userId", userId);
        } else {
            userId = ( String ) session.getAttribute("userId");
        }
        return userId;
    }
}
