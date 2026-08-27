/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.web.service;

import com.dingyangmall.common.core.domain.model.LoginUser;
import com.dingyangmall.common.core.redis.RedisCache;
import com.dingyangmall.common.utils.ServletUtils;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.common.utils.ip.AddressUtils;
import com.dingyangmall.common.utils.ip.IpUtils;
import com.dingyangmall.common.utils.uuid.IdUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenService {
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);
    @Value(value="${token.header}")
    private String header;
    @Value(value="${token.secret}")
    private String secret;
    @Value(value="${token.expireTime}")
    private int expireTime;
    protected static final long MILLIS_SECOND = 1000L;
    protected static final long MILLIS_MINUTE = 60000L;
    private static final Long MILLIS_MINUTE_TEN = 1200000L;
    @Autowired
    private RedisCache redisCache;

    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = this.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            try {
                Claims claims = this.parseToken(token);
                String uuid = (String)claims.get("login_user_key");
                String userKey = this.getTokenKey(uuid);
                LoginUser user = (LoginUser)this.redisCache.getCacheObject(userKey);
                return user;
            }
            catch (Exception e) {
                log.error("\u83b7\u53d6\u7528\u6237\u4fe1\u606f\u5f02\u5e38'{}'", (Object)e.getMessage());
            }
        }
        return null;
    }

    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            this.refreshToken(loginUser);
        }
    }

    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = this.getTokenKey(token);
            this.redisCache.deleteObject(userKey);
        }
    }

    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        this.setUserAgent(loginUser);
        this.refreshToken(loginUser);
        HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("login_user_key", token);
        return this.createToken(claims);
    }

    public void verifyToken(LoginUser loginUser) {
        long currentTime;
        long expireTime = loginUser.getExpireTime();
        if (expireTime - (currentTime = System.currentTimeMillis()) <= MILLIS_MINUTE_TEN) {
            this.refreshToken(loginUser);
        }
    }

    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + (long)this.expireTime * 60000L);
        String userKey = this.getTokenKey(loginUser.getToken());
        this.redisCache.setCacheObject(userKey, loginUser, this.expireTime, TimeUnit.MINUTES);
    }

    public String createLongTermToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        this.setUserAgent(loginUser);
        int appExpireTime = 259200;
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + (long)appExpireTime * 60000L);
        String userKey = this.getTokenKey(token);
        this.redisCache.setCacheObject(userKey, loginUser, appExpireTime, TimeUnit.MINUTES);
        HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("login_user_key", token);
        return this.createToken(claims);
    }

    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr();
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    private String createToken(Map<String, Object> claims) {
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, this.secret).compact();
        return token;
    }

    private Claims parseToken(String token) {
        return (Claims)Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = this.parseToken(token);
        return claims.getSubject();
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(this.header);
        if (StringUtils.isNotEmpty(token) && token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
            return token;
        }
        String wxToken = request.getHeader("X-Wx-Token");
        if (StringUtils.isNotEmpty(wxToken)) {
            return wxToken.trim();
        }
        return token;
    }

    private String getTokenKey(String uuid) {
        return "login_tokens:" + uuid;
    }
}

