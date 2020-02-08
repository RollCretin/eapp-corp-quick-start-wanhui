/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: TokenInfoConfig
 * Author:   cretin
 * Date:     12/26/18 09:28
 * Description: 记录token的过期时间
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.config;

/**
 * 〈记录token的过期时间〉
 *
 * @author cretin
 * @create 12/26/18
 * @since 1.0.0
 */
public class TokenInfoConfig {
    private static TokenInfoConfig tokenInfo;

    public static TokenInfoConfig getInstance() {
        if ( tokenInfo == null )
            tokenInfo = new TokenInfoConfig("", 0);
        return tokenInfo;
    }

    private String accessToken;
    //过期时间
    private long lastExpiresTime;

    public TokenInfoConfig(String accessToken, long lastExpiresTime) {
        this.accessToken = accessToken;
        this.lastExpiresTime = lastExpiresTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getLastExpiresTime() {
        return lastExpiresTime;
    }

    public void setLastExpiresTime(long lastExpiresTime) {
        this.lastExpiresTime = lastExpiresTime;
    }
}