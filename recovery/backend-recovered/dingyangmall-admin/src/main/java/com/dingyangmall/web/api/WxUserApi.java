/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.api;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.common.core.domain.model.LoginUser;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.common.utils.uuid.IdUtils;
import com.dingyangmall.framework.web.service.TokenService;
import com.dingyangmall.mall.dto.DynamicCodeDTO;
import com.dingyangmall.mall.entity.TbCouponInfo;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.DynamicCodeService;
import com.dingyangmall.mall.service.TbCouponInfoService;
import com.dingyangmall.mall.service.TbIntegralRuleService;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.system.service.ISysUserService;
import com.dingyangmall.web.entity.WxMaUser;
import com.dingyangmall.web.mapper.WxMaUserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import me.chanjar.weixin.common.error.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/weixin/api/ma/wxuser", "/api/ma/wxuser", "/wxuser"})
public class WxUserApi {
    private static final Logger log = LoggerFactory.getLogger(WxUserApi.class);
    private static final String REDIS_OPENID_SESSION_PREFIX = "wx:openid_session:";
    private static final String REDIS_OPENID_TO_TOKEN_PREFIX = "wx:openid_to_token:";
    private static final String REDIS_USER_INFO_PREFIX = "wx:user:info:";
    private static final String REDIS_CODE_USED_PREFIX = "wx:code_used:";
    private static final String REDIS_CODE_TO_OPENID_PREFIX = "wx:code_to_openid:";
    private static final long SESSION_EXPIRE_DAYS = 7L;
    private static final long USER_INFO_EXPIRE_DAYS = 30L;
    private static final long CODE_USED_EXPIRE_MINUTES = 6L;
    @Autowired(required=false)
    private WxMaService wxMaService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired(required=false)
    private WxMaUserMapper wxMaUserMapper;
    @Autowired(required=false)
    private UmsMemberService umsMemberService;
    @Autowired(required=false)
    private TbCouponInfoService tbCouponInfoService;
    @Autowired(required=false)
    private TbIntegralRuleService tbIntegralRuleService;
    @Autowired(required=false)
    private DynamicCodeService dynamicCodeService;
    @Autowired(required=false)
    private TokenService tokenService;
    @Autowired(required=false)
    private ISysUserService sysUserService;
    @Value(value="${wx.ma.session-use-redis:true}")
    private boolean sessionUseRedis;

    private static boolean isOpenidFormat(String s) {
        return StringUtils.isNotEmpty(s) && s.length() >= 26 && s.length() <= 32 && s.startsWith("o");
    }

    private static String getTokenFromHeaders(HttpServletRequest request) {
        String v = request.getHeader("X-Wx-Token");
        if (StringUtils.isNotEmpty(v)) {
            return v.trim();
        }
        String auth = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(auth) && auth.startsWith("Bearer ") && StringUtils.isNotEmpty(v = auth.substring("Bearer ".length()).trim())) {
            return v;
        }
        return null;
    }

    private static String getTokenFromBody(Map<String, String> body) {
        if (body == null) {
            return null;
        }
        String v = body.get("token");
        return StringUtils.isNotEmpty(v) ? v.trim() : null;
    }

    private static String resolveToken(HttpServletRequest request, Map<String, String> body) {
        String token = WxUserApi.getTokenFromHeaders(request);
        if (StringUtils.isEmpty(token)) {
            token = WxUserApi.getTokenFromBody(body);
        }
        return token;
    }

    private String getSessionRedisKey(String openid) {
        if (!WxUserApi.isOpenidFormat(openid)) {
            return null;
        }
        return REDIS_OPENID_SESSION_PREFIX + openid;
    }

    private Map<String, String> getSessionData(String openid) {
        if (!WxUserApi.isOpenidFormat(openid)) {
            return null;
        }
        if (!this.sessionUseRedis) {
            if (this.wxMaUserMapper == null) {
                return null;
            }
            WxMaUser db = this.wxMaUserMapper.selectByOpenid(openid);
            if (db == null) {
                return null;
            }
            HashMap<String, String> m = new HashMap<String, String>();
            m.put("openid", openid);
            m.put("memberId", openid);
            if (StringUtils.isNotEmpty(db.getNickname())) {
                m.put("nickname", db.getNickname());
            }
            if (StringUtils.isNotEmpty(db.getAvatarUrl())) {
                m.put("avatarUrl", db.getAvatarUrl());
            }
            return m;
        }
        String key = this.getSessionRedisKey(openid);
        if (key == null) {
            return null;
        }
        Object v = this.redisTemplate.opsForValue().get(key);
        return v instanceof Map ? (Map)v : null;
    }

    private Map<String, String> getSessionDataByToken(String token, HttpServletRequest request) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        if (this.sessionUseRedis) {
            String key = "wx:token:" + token;
            Object v = this.redisTemplate.opsForValue().get(key);
            if (v instanceof Map) {
                return (Map)v;
            }
        }
        if (this.tokenService != null && request != null) {
            try {
                LoginUser loginUser = this.tokenService.getLoginUser(request);
                if (loginUser != null && loginUser.getUser() != null) {
                    HashMap<String, String> sessionData = new HashMap<String, String>();
                    sessionData.put("memberId", String.valueOf(loginUser.getUser().getUserId()));
                    sessionData.put("userId", String.valueOf(loginUser.getUser().getUserId()));
                    sessionData.put("phone", loginUser.getUser().getUserName());
                    sessionData.put("phoneNumber", loginUser.getUser().getUserName());
                    sessionData.put("nickname", loginUser.getUser().getNickName());
                    return sessionData;
                }
            }
            catch (Exception e) {
                log.debug("JWT token \u89e3\u6790\u5931\u8d25: {}", (Object)e.getMessage());
            }
        }
        return null;
    }

    private Map<String, Object> buildLoginResponseMap(String openid, Map<String, String> sessionData, String token) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("token", token);
        data.put("userId", openid);
        data.put("openid", openid);
        if (sessionData != null) {
            if (StringUtils.isNotEmpty(sessionData.get("unionid"))) {
                data.put("unionid", sessionData.get("unionid"));
            }
            if (StringUtils.isNotEmpty(sessionData.get("nickname"))) {
                data.put("nickname", sessionData.get("nickname"));
            }
            if (StringUtils.isNotEmpty(sessionData.get("avatarUrl"))) {
                data.put("avatarUrl", sessionData.get("avatarUrl"));
            }
        }
        return data;
    }

    @Operation(summary="\u5c0f\u7a0b\u5e8f\u767b\u5f55", description="\u5fae\u4fe1\u5b98\u65b9\u6d41\u7a0b\uff1awx.login \u83b7\u53d6 code\uff0c\u672c\u63a5\u53e3\u8c03\u7528 code2Session \u6362\u53d6 openid/session_key\uff0c\u7b7e\u53d1 token \u5b58 Redis\uff1b\u8fd4\u56de token\uff0c\u540e\u7eed\u8bf7\u6c42 Header \u5e26 X-Wx-Token\u3002")
    @ApiResponses(value={@ApiResponse(responseCode="200", description="\u6210\u529f", content={@Content(schema=@Schema(implementation=WxLoginVO.class))})})
    @PostMapping(value={"/login"})
    public AjaxResult login(@RequestBody Map<String, String> body) {
        String code;
        String string = body != null ? (body.get("code") != null ? body.get("code") : body.get("jsCode")) : (code = null);
        if (code != null) {
            code = code.trim();
        }
        if (StringUtils.isEmpty(code)) {
            return AjaxResult.error("\u7f3a\u5c11\u53c2\u6570 code \u6216 jsCode\uff08\u5c0f\u7a0b\u5e8f\u9700\u5148\u8c03\u7528 wx.login \u83b7\u53d6 code\uff09");
        }
        if (this.wxMaService == null) {
            log.warn("\u5c0f\u7a0b\u5e8f\u767b\u5f55\u5931\u8d25\uff1a\u672a\u914d\u7f6e WxMaService\uff0c\u8bf7\u68c0\u67e5 wx.ma.configs");
            return AjaxResult.error("\u5c0f\u7a0b\u5e8f\u672a\u914d\u7f6e\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458");
        }
        String codeUsedKey = REDIS_CODE_USED_PREFIX + code;
        String codeToOpenidKey = REDIS_CODE_TO_OPENID_PREFIX + code;
        if (this.sessionUseRedis && Boolean.TRUE.equals(this.redisTemplate.hasKey(codeUsedKey))) {
            String openid;
            Map<String, String> existingData;
            Object existingOpenid = this.redisTemplate.opsForValue().get(codeToOpenidKey);
            if (existingOpenid instanceof String && (existingData = this.getSessionData(openid = (String)existingOpenid)) != null) {
                String existingToken = (String)this.redisTemplate.opsForValue().get(REDIS_OPENID_TO_TOKEN_PREFIX + openid);
                if (StringUtils.isEmpty(existingToken)) {
                    existingToken = IdUtils.simpleUUID();
                    this.redisTemplate.opsForValue().set("wx:token:" + existingToken, existingData, 7L, TimeUnit.DAYS);
                    this.redisTemplate.opsForValue().set(REDIS_OPENID_TO_TOKEN_PREFIX + openid, existingToken, 7L, TimeUnit.DAYS);
                }
                return AjaxResult.success(this.buildLoginResponseMap(openid, existingData, existingToken));
            }
            log.warn("\u767b\u5f55\u51ed\u8bc1\u5df2\u4f7f\u7528\u4e14\u65e0\u6cd5\u590d\u7528\u4f1a\u8bdd\uff0c\u8bf7\u7528\u6237\u91cd\u65b0\u70b9\u51fb\u767b\u5f55");
            return AjaxResult.error("\u8be5\u767b\u5f55\u51ed\u8bc1\u5df2\u4f7f\u7528\uff0c\u8bf7\u91cd\u65b0\u70b9\u51fb\u300c\u5fae\u4fe1\u4e00\u952e\u767b\u5f55\u300d\u83b7\u53d6\u65b0\u51ed\u8bc1");
        }
        try {
            String token;
            Map info;
            WxMaUser dbUser;
            WxMaJscode2SessionResult session = this.wxMaService.getUserService().getSessionInfo(code);
            String openid = session.getOpenid();
            String sessionKey = session.getSessionKey();
            if (StringUtils.isEmpty(openid)) {
                return AjaxResult.error("\u5fae\u4fe1\u767b\u5f55\u5931\u8d25\uff0copenid \u4e3a\u7a7a");
            }
            String redisKey = REDIS_OPENID_SESSION_PREFIX + openid;
            HashMap<String, String> sessionData = new HashMap<String, String>();
            sessionData.put("openid", openid);
            sessionData.put("memberId", openid);
            sessionData.put("sessionKey", sessionKey);
            if (StringUtils.isNotEmpty(session.getUnionid())) {
                sessionData.put("unionid", session.getUnionid());
            }
            if (this.wxMaUserMapper != null) {
                try {
                    WxMaUser u = new WxMaUser();
                    u.setId(IdUtils.simpleUUID());
                    u.setOpenid(openid);
                    u.setUnionid(session.getUnionid());
                    this.wxMaUserMapper.upsert(u);
                }
                catch (Exception e) {
                    log.warn("wx_user \u843d\u5e93\u5931\u8d25: {}", (Object)e.getMessage());
                }
            }
            if (this.sessionUseRedis) {
                Object userInfoObj = this.redisTemplate.opsForValue().get(REDIS_USER_INFO_PREFIX + openid);
                if (userInfoObj instanceof Map) {
                    Map userInfo = (Map)userInfoObj;
                    String nickname = (String)userInfo.get("nickname");
                    String avatarUrl = (String)userInfo.get("avatarUrl");
                    if (StringUtils.isNotEmpty(nickname)) {
                        sessionData.put("nickname", nickname);
                    }
                    if (StringUtils.isNotEmpty(avatarUrl)) {
                        sessionData.put("avatarUrl", avatarUrl);
                    }
                }
                this.redisTemplate.opsForValue().set(redisKey, sessionData, 7L, TimeUnit.DAYS);
                String token2 = IdUtils.simpleUUID();
                this.redisTemplate.opsForValue().set("wx:token:" + token2, sessionData, 7L, TimeUnit.DAYS);
                this.redisTemplate.opsForValue().set(REDIS_OPENID_TO_TOKEN_PREFIX + openid, token2, 7L, TimeUnit.DAYS);
                this.redisTemplate.opsForValue().set(codeToOpenidKey, openid, 6L, TimeUnit.MINUTES);
                this.redisTemplate.opsForValue().set(codeUsedKey, "1", 6L, TimeUnit.MINUTES);
                if (log.isInfoEnabled()) {
                    log.info("[WxMa] \u767b\u5f55\u6210\u529f\uff0c\u5df2\u7b7e\u53d1 token\uff0cRedis key={} expire={}\u5929", (Object)("wx:token:" + token2), (Object)7L);
                }
            } else if (log.isInfoEnabled()) {
                log.info("[WxMa] \u767b\u5f55\u6210\u529f\uff0c\u5df2\u5173\u95ed Redis\uff0c\u4ec5\u5199 DB openid={}", openid != null ? openid.substring(0, Math.min(12, openid.length())) + "..." : "");
            }
            if (this.wxMaUserMapper != null && (dbUser = this.wxMaUserMapper.selectByOpenid(openid)) != null) {
                if (StringUtils.isNotEmpty(dbUser.getNickname())) {
                    sessionData.put("nickname", dbUser.getNickname());
                }
                if (StringUtils.isNotEmpty(dbUser.getAvatarUrl())) {
                    sessionData.put("avatarUrl", dbUser.getAvatarUrl());
                }
            }
            if (!(!this.sessionUseRedis || sessionData.containsKey("nickname") && sessionData.containsKey("avatarUrl") || (info = (Map)this.redisTemplate.opsForValue().get(REDIS_USER_INFO_PREFIX + openid)) == null)) {
                if (!sessionData.containsKey("nickname") && StringUtils.isNotEmpty((String)info.get("nickname"))) {
                    sessionData.put("nickname", (String)info.get("nickname"));
                }
                if (!sessionData.containsKey("avatarUrl") && StringUtils.isNotEmpty((String)info.get("avatarUrl"))) {
                    sessionData.put("avatarUrl", (String)info.get("avatarUrl"));
                }
            }
            String string2 = token = this.sessionUseRedis ? (String)this.redisTemplate.opsForValue().get(REDIS_OPENID_TO_TOKEN_PREFIX + openid) : IdUtils.simpleUUID();
            if (StringUtils.isEmpty(token)) {
                token = IdUtils.simpleUUID();
            }
            return AjaxResult.success(this.buildLoginResponseMap(openid, sessionData, token));
        }
        catch (WxErrorException e) {
            log.warn("\u5fae\u4fe1 code2Session \u5931\u8d25: errCode={}, errMsg={}", (Object)e.getError().getErrorCode(), (Object)e.getMessage());
            int errCode = e.getError().getErrorCode();
            if (errCode == 40013) {
                return AjaxResult.error("\u5c0f\u7a0b\u5e8f appId \u672a\u914d\u7f6e\u6216\u9519\u8bef\uff0c\u8bf7\u5728 application.yml \u7684 wx.ma.configs \u4e2d\u586b\u5199\u6b63\u786e\u7684\u5c0f\u7a0b\u5e8f AppID \u548c AppSecret\uff08\u5fae\u4fe1\u516c\u4f17\u5e73\u53f0-\u5f00\u53d1-\u5f00\u53d1\u7ba1\u7406\uff09");
            }
            if (errCode == 40125) {
                return AjaxResult.error("\u5c0f\u7a0b\u5e8f appSecret \u9519\u8bef\uff0c\u8bf7\u68c0\u67e5 wx.ma.configs \u4e2d\u7684 secret \u662f\u5426\u4e0e\u5fae\u4fe1\u516c\u4f17\u5e73\u53f0\u4e00\u81f4");
            }
            if (errCode == 40029) {
                return AjaxResult.error("\u767b\u5f55\u51ed\u8bc1\u65e0\u6548\u6216\u5df2\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u6253\u5f00\u5c0f\u7a0b\u5e8f\u5e76\u8c03\u7528 wx.login \u540e\u91cd\u8bd5");
            }
            if (errCode == 40163 && this.sessionUseRedis) {
                String openid;
                Map<String, String> existingData;
                Object existingOpenid = this.redisTemplate.opsForValue().get(codeToOpenidKey);
                if (existingOpenid instanceof String && (existingData = this.getSessionData(openid = (String)existingOpenid)) != null) {
                    String existingToken = (String)this.redisTemplate.opsForValue().get(REDIS_OPENID_TO_TOKEN_PREFIX + openid);
                    if (StringUtils.isEmpty(existingToken)) {
                        existingToken = IdUtils.simpleUUID();
                        this.redisTemplate.opsForValue().set("wx:token:" + existingToken, existingData, 7L, TimeUnit.DAYS);
                        this.redisTemplate.opsForValue().set(REDIS_OPENID_TO_TOKEN_PREFIX + openid, existingToken, 7L, TimeUnit.DAYS);
                    }
                    return AjaxResult.success(this.buildLoginResponseMap(openid, existingData, existingToken));
                }
                return AjaxResult.error("\u8be5\u767b\u5f55\u51ed\u8bc1\u5df2\u88ab\u4f7f\u7528\uff0c\u8bf7\u91cd\u65b0\u70b9\u51fb\u300c\u5fae\u4fe1\u4e00\u952e\u767b\u5f55\u300d");
            }
            return AjaxResult.error("\u5fae\u4fe1\u767b\u5f55\u5931\u8d25\uff1a" + (e.getError().getErrorMsg() != null ? e.getError().getErrorMsg() : e.getMessage()));
        }
        catch (RuntimeException e) {
            for (Throwable c = e; c != null; c = c.getCause()) {
                String msg;
                String string3 = msg = c.getMessage() != null ? c.getMessage() : "";
                if (!msg.contains("Connect timed out") && !msg.contains("ConnectTimeout") && !c.getClass().getSimpleName().contains("Timeout")) continue;
                log.warn("\u8fde\u63a5\u5fae\u4fe1\u670d\u52a1\u5668\u8d85\u65f6\uff0c\u8bf7\u68c0\u67e5\u672c\u673a\u7f51\u7edc\u3001DNS \u6216\u4ee3\u7406: {}", (Object)e.getMessage());
                return AjaxResult.error("\u8fde\u63a5\u5fae\u4fe1\u670d\u52a1\u5668\u8d85\u65f6\uff0c\u8bf7\u68c0\u67e5\u670d\u52a1\u5668\u7f51\u7edc\u6216\u914d\u7f6e\u4ee3\u7406\u540e\u91cd\u8bd5");
            }
            throw e;
        }
    }

    @Operation(summary="\u83b7\u53d6\u5f53\u524d\u7528\u6237\u4fe1\u606f", description="\u8fd4\u56de userId\u3001\u6635\u79f0\u3001\u5934\u50cf\uff1b\u9700\u5728 Header \u643a\u5e26 X-Wx-Token")
    @ApiResponses(value={@ApiResponse(responseCode="200", description="\u6210\u529f", content={@Content(schema=@Schema(implementation=WxUserInfoVO.class))})})
    private static String tokenErrorMsg(String token) {
        if (StringUtils.isEmpty(token)) {
            return "\u672a\u767b\u5f55\u6216\u767b\u5f55\u5df2\u8fc7\u671f\uff08\u539f\u56e0\uff1a\u8bf7\u6c42\u672a\u643a\u5e26 Header X-Wx-Token \u6216 body.token\uff09";
        }
        return "\u672a\u767b\u5f55\u6216\u767b\u5f55\u5df2\u8fc7\u671f\uff08\u539f\u56e0\uff1a\u4ee4\u724c\u65e0\u6548\u6216\u5df2\u8fc7\u671f\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55\uff09";
    }

    @GetMapping(value={"/info"})
    public AjaxResult getUserInfo(HttpServletRequest request) {
        HashMap<String, Object> info;
        block18: {
            WxMaUser dbUser;
            String phone;
            String token = WxUserApi.resolveToken(request, null);
            Map<String, String> sessionData = this.getSessionDataByToken(token, request);
            if (sessionData == null) {
                return AjaxResult.error(WxUserApi.tokenErrorMsg(token));
            }
            String openid = sessionData.get("openid");
            if (StringUtils.isEmpty(openid)) {
                openid = sessionData.get("memberId");
            }
            info = new HashMap<String, Object>();
            info.put("userId", openid);
            info.put("openid", openid);
            if (StringUtils.isNotEmpty(sessionData.get("nickname"))) {
                info.put("nickname", sessionData.get("nickname"));
            }
            if (StringUtils.isNotEmpty(sessionData.get("avatarUrl"))) {
                info.put("avatarUrl", sessionData.get("avatarUrl"));
            }
            if (StringUtils.isEmpty(phone = sessionData.get("phoneNumber"))) {
                phone = sessionData.get("phone");
            }
            if (StringUtils.isEmpty(phone) && this.wxMaUserMapper != null && (dbUser = this.wxMaUserMapper.selectByOpenid(openid)) != null && StringUtils.isNotEmpty(dbUser.getPhone())) {
                phone = dbUser.getPhone();
            }
            if (StringUtils.isNotEmpty(phone)) {
                info.put("phoneNumber", phone);
                info.put("phone", phone);
                if (this.umsMemberService != null) {
                    UmsMember member = this.umsMemberService.getByPhone(phone);
                    if (member != null) {
                        info.put("points", member.getPoints() != null ? member.getPoints() : 0);
                        info.put("balance", member.getBalance() != null ? member.getBalance() : Integer.valueOf(0));
                        info.put("level", member.getLevel() != null ? member.getLevel() : 0);
                    } else {
                        info.put("points", 0);
                        info.put("balance", 0);
                        info.put("level", 0);
                    }
                }
                log.info("\u5f00\u59cb\u67e5\u8be2\u7ecf\u9500\u5546\u4fe1\u606f\uff0c\u624b\u673a\u53f7: {}", (Object)phone);
                if (this.sysUserService != null) {
                    try {
                        SysUser sysUser = this.sysUserService.selectUserByUserName(phone);
                        if (sysUser == null) {
                            sysUser = this.sysUserService.selectUserByPhoneNumber(phone);
                            log.info("\u901a\u8fc7\u624b\u673a\u53f7\u5b57\u6bb5\u67e5\u8be2\u7cfb\u7edf\u7528\u6237: {}", (Object)(sysUser != null ? sysUser.getUserName() : "null"));
                        } else {
                            log.info("\u901a\u8fc7\u7528\u6237\u540d\u67e5\u8be2\u5230\u7cfb\u7edf\u7528\u6237: {}", (Object)sysUser.getUserName());
                        }
                        log.info("\u67e5\u8be2\u5230\u7cfb\u7edf\u7528\u6237: {}, dealerLevel: {}", (Object)(sysUser != null ? sysUser.getUserName() : "null"), sysUser != null ? sysUser.getDealerLevel() : "null");
                        if (sysUser != null && sysUser.getDealerLevel() != null && sysUser.getDealerLevel() > 0) {
                            info.put("isDealer", true);
                            info.put("dealerLevel", sysUser.getDealerLevel());
                            if (info.get("level") == null || (Integer)info.get("level") == 0) {
                                info.put("level", sysUser.getDealerLevel());
                            }
                            log.info("\u7528\u6237\u662f\u7ecf\u9500\u5546\uff0c\u7b49\u7ea7: {}", (Object)sysUser.getDealerLevel());
                            break block18;
                        }
                        log.info("\u7528\u6237\u4e0d\u662f\u7ecf\u9500\u5546\u6216dealerLevel\u4e3a\u7a7a");
                    }
                    catch (Exception e) {
                        log.error("\u67e5\u8be2\u7ecf\u9500\u5546\u4fe1\u606f\u5931\u8d25: {}", (Object)e.getMessage(), (Object)e);
                    }
                } else {
                    log.warn("sysUserService \u4e3a null\uff0c\u65e0\u6cd5\u67e5\u8be2\u7ecf\u9500\u5546\u4fe1\u606f");
                }
            }
        }
        return AjaxResult.success(info);
    }

    @Operation(summary="\u66f4\u65b0\u7528\u6237\u4fe1\u606f", description="\u652f\u6301 encryptedData+iv \u89e3\u5bc6\u6216\u660e\u6587 nickname/avatarUrl\uff1b\u9700\u5728 Header \u643a\u5e26 X-Wx-Token")
    @ApiResponses(value={@ApiResponse(responseCode="200", description="\u6210\u529f", content={@Content(schema=@Schema(implementation=WxUserInfoVO.class))})})
    @PostMapping
    public AjaxResult updateUser(HttpServletRequest request, @RequestBody Map<String, String> body) {
        if (body == null || body.isEmpty()) {
            return AjaxResult.error("\u53c2\u6570\u4e0d\u80fd\u4e3a\u7a7a");
        }
        String token = WxUserApi.resolveToken(request, body);
        Map<String, String> sessionData = this.getSessionDataByToken(token, request);
        if (sessionData == null) {
            return AjaxResult.error(WxUserApi.tokenErrorMsg(token));
        }
        String openid = sessionData.get("openid");
        if (StringUtils.isEmpty(openid)) {
            openid = sessionData.get("memberId");
        }
        String sessionKey = sessionData.get("sessionKey");
        String nickname = null;
        String avatarUrl = null;
        String encryptedData = body.get("encryptedData");
        String iv = body.get("iv");
        if (StringUtils.isNotEmpty(encryptedData) && StringUtils.isNotEmpty(iv) && StringUtils.isNotEmpty(sessionKey) && this.wxMaService != null) {
            try {
                WxMaUserInfo userInfo = this.wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
                if (userInfo != null) {
                    nickname = userInfo.getNickName();
                    avatarUrl = userInfo.getAvatarUrl();
                }
            }
            catch (Exception e) {
                log.warn("\u89e3\u5bc6\u7528\u6237\u4fe1\u606f\u5931\u8d25: {}", (Object)e.getMessage());
                return AjaxResult.error("\u89e3\u5bc6\u7528\u6237\u4fe1\u606f\u5931\u8d25");
            }
        }
        if (nickname == null && avatarUrl == null) {
            nickname = body.get("nickname");
            if (StringUtils.isEmpty(nickname)) {
                nickname = body.get("nickName");
            }
            avatarUrl = body.get("avatarUrl");
        }
        String ui = body.get("userInfo");
        if ((StringUtils.isEmpty(nickname) || StringUtils.isEmpty(avatarUrl)) && ui instanceof Map) {
            Map userInfoMap = (Map)((Object)ui);
            if (StringUtils.isEmpty(nickname)) {
                Object nn = userInfoMap.get("nickName");
                if (nn == null) {
                    nn = userInfoMap.get("nickname");
                }
                if (nn != null) {
                    nickname = String.valueOf(nn);
                }
            }
            if (StringUtils.isEmpty(avatarUrl)) {
                Object av = userInfoMap.get("avatarUrl");
                if (av == null) {
                    av = userInfoMap.get("headimgUrl");
                }
                if (av == null) {
                    av = userInfoMap.get("avatar");
                }
                if (av != null) {
                    avatarUrl = String.valueOf(av);
                }
            }
        }
        if (StringUtils.isEmpty(nickname) && StringUtils.isEmpty(avatarUrl)) {
            return AjaxResult.success();
        }
        if (StringUtils.isNotEmpty(nickname)) {
            sessionData.put("nickname", nickname);
        }
        if (StringUtils.isNotEmpty(avatarUrl)) {
            sessionData.put("avatarUrl", avatarUrl);
        }
        sessionData.put("memberId", openid);
        sessionData.put("openid", openid);
        if (this.sessionUseRedis) {
            String redisKey = this.getSessionRedisKey(openid);
            if (redisKey != null) {
                this.redisTemplate.opsForValue().set(redisKey, sessionData, 7L, TimeUnit.DAYS);
            }
            if (StringUtils.isNotEmpty(token)) {
                this.redisTemplate.opsForValue().set("wx:token:" + token, sessionData, 7L, TimeUnit.DAYS);
            }
        }
        HashMap<String, String> userInfo = this.sessionUseRedis ? (HashMap<String, String>)this.redisTemplate.opsForValue().get(REDIS_USER_INFO_PREFIX + openid) : null;
        userInfo = userInfo == null ? new HashMap<String, String>() : new HashMap(userInfo);
        if (StringUtils.isNotEmpty(nickname)) {
            userInfo.put("nickname", nickname);
        }
        if (StringUtils.isNotEmpty(avatarUrl)) {
            userInfo.put("avatarUrl", avatarUrl);
        }
        if (this.sessionUseRedis && !userInfo.isEmpty()) {
            this.redisTemplate.opsForValue().set(REDIS_USER_INFO_PREFIX + openid, userInfo, 30L, TimeUnit.DAYS);
        }
        if (this.wxMaUserMapper != null) {
            WxMaUser u = new WxMaUser();
            u.setId(IdUtils.simpleUUID());
            u.setOpenid(openid);
            if (StringUtils.isNotEmpty(sessionData.get("unionid"))) {
                u.setUnionid(sessionData.get("unionid"));
            }
            if (StringUtils.isNotEmpty(nickname)) {
                u.setNickname(nickname);
            }
            if (StringUtils.isNotEmpty(avatarUrl)) {
                u.setAvatarUrl(avatarUrl);
            }
            this.wxMaUserMapper.upsert(u);
        }
        HashMap<String, String> result = new HashMap<String, String>();
        result.put("userId", openid);
        result.put("openid", openid);
        if (StringUtils.isNotEmpty(nickname)) {
            result.put("nickname", nickname);
        }
        if (StringUtils.isNotEmpty(avatarUrl)) {
            result.put("avatarUrl", avatarUrl);
        }
        return AjaxResult.success(result);
    }

    @Operation(summary="\u83b7\u53d6\u624b\u673a\u53f7", description="\u524d\u7aef getPhoneNumber \u56de\u8c03\u4e2d\u7684 code \u4f20\u5230\u6b64\u63a5\u53e3\uff0c\u540e\u7aef\u8c03\u5fae\u4fe1 getuserphonenumber \u89e3\u5bc6\uff1b\u9700\u767b\u5f55\u6001(Header X-Wx-Token)\u4e14\u5c0f\u7a0b\u5e8f\u5df2\u5f00\u901a\u624b\u673a\u53f7\u80fd\u529b\u5e76\u4f01\u4e1a\u8ba4\u8bc1\u3002")
    @ApiResponses(value={@ApiResponse(responseCode="200", description="\u6210\u529f\uff0c\u8fd4\u56de phoneNumber\u3001purePhoneNumber \u7b49"), @ApiResponse(responseCode="400", description="code \u65e0\u6548\u6216\u672a\u5f00\u901a\u624b\u673a\u53f7\u80fd\u529b")})
    @PostMapping(value={"/phone"})
    public AjaxResult getPhoneNumber(HttpServletRequest request, @RequestBody Map<String, String> body) {
        String code;
        String string = code = body != null ? body.get("code") : null;
        if (code != null) {
            code = code.trim();
        }
        if (StringUtils.isEmpty(code)) {
            return AjaxResult.error("\u7f3a\u5c11\u53c2\u6570 code\uff08\u9700\u524d\u7aef getPhoneNumber \u56de\u8c03\u4f20\u5165\uff09");
        }
        String token = WxUserApi.resolveToken(request, body);
        Map<String, String> sessionData = this.getSessionDataByToken(token, request);
        if (sessionData == null) {
            return AjaxResult.error(WxUserApi.tokenErrorMsg(token));
        }
        if (this.wxMaService == null) {
            return AjaxResult.error("\u5c0f\u7a0b\u5e8f\u672a\u914d\u7f6e");
        }
        try {
            WxMaPhoneNumberInfo phoneInfo = this.wxMaService.getUserService().getPhoneNumber(code);
            if (phoneInfo == null) {
                return AjaxResult.error("\u83b7\u53d6\u624b\u673a\u53f7\u5931\u8d25");
            }
            String phoneNumber = phoneInfo.getPhoneNumber();
            String purePhoneNumber = phoneInfo.getPurePhoneNumber();
            if (StringUtils.isEmpty(phoneNumber)) {
                phoneNumber = purePhoneNumber;
            }
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("phoneNumber", phoneNumber);
            data.put("purePhoneNumber", purePhoneNumber);
            data.put("countryCode", phoneInfo.getCountryCode());
            String openid = sessionData.get("openid");
            if (StringUtils.isEmpty(openid)) {
                openid = sessionData.get("memberId");
            }
            if (StringUtils.isNotEmpty(openid) && StringUtils.isNotEmpty(phoneNumber)) {
                sessionData.put("phoneNumber", phoneNumber);
                sessionData.put("memberId", openid);
                sessionData.put("openid", openid);
                if (this.sessionUseRedis) {
                    String redisKey = this.getSessionRedisKey(openid);
                    if (redisKey != null) {
                        this.redisTemplate.opsForValue().set(redisKey, sessionData, 7L, TimeUnit.DAYS);
                    }
                    if (StringUtils.isNotEmpty(token)) {
                        this.redisTemplate.opsForValue().set("wx:token:" + token, sessionData, 7L, TimeUnit.DAYS);
                    }
                }
                if (this.wxMaUserMapper != null) {
                    try {
                        this.wxMaUserMapper.updatePhoneByOpenid(openid, phoneNumber);
                    }
                    catch (Exception ex) {
                        log.warn("\u66f4\u65b0 wx_user \u624b\u673a\u53f7\u5931\u8d25: openid={}, {}", (Object)openid, (Object)ex.getMessage());
                    }
                }
            }
            return AjaxResult.success(data);
        }
        catch (WxErrorException e) {
            log.warn("\u83b7\u53d6\u624b\u673a\u53f7\u5931\u8d25: errCode={}, errMsg={}", (Object)e.getError().getErrorCode(), (Object)e.getError().getErrorMsg());
            String msg = e.getError().getErrorMsg();
            if (msg != null && (msg.contains("phone") || msg.contains("\u6743\u9650") || msg.contains("\u80fd\u529b"))) {
                return AjaxResult.error("\u672a\u5f00\u901a\u624b\u673a\u53f7\u80fd\u529b\u6216\u672a\u4f01\u4e1a\u8ba4\u8bc1\uff0c\u8bf7\u5728\u5fae\u4fe1\u516c\u4f17\u5e73\u53f0\u5f00\u901a");
            }
            return AjaxResult.error(msg != null ? msg : "\u83b7\u53d6\u624b\u673a\u53f7\u5931\u8d25");
        }
    }

    @Operation(summary="\u83b7\u53d6\u4f1a\u5458\u7801", description="\u9700\u767b\u5f55\u4e14\u5df2\u7ed1\u5b9a\u624b\u673a\u53f7\uff1b\u8fd4\u56de\u4f1a\u5458\u7801\u53ca\u7b80\u8981\u4fe1\u606f\uff0c\u7528\u4e8e\u51fa\u793a\u7ed9\u5546\u5bb6\u626b\u7801")
    @GetMapping(value={"/member-code"})
    public AjaxResult getMemberCode(HttpServletRequest request) {
        WxMaUser dbUser;
        String avatarUrl;
        WxMaUser dbUser2;
        WxMaUser dbUser3;
        String phone;
        String token = WxUserApi.resolveToken(request, null);
        Map<String, String> sessionData = this.getSessionDataByToken(token, request);
        if (sessionData == null) {
            return AjaxResult.error(WxUserApi.tokenErrorMsg(token));
        }
        String openid = sessionData.get("openid");
        if (StringUtils.isEmpty(openid)) {
            openid = sessionData.get("memberId");
        }
        if (StringUtils.isEmpty(phone = sessionData.get("phoneNumber"))) {
            phone = sessionData.get("phone");
        }
        if (StringUtils.isEmpty(phone) && this.wxMaUserMapper != null && (dbUser3 = this.wxMaUserMapper.selectByOpenid(openid)) != null && StringUtils.isNotEmpty(dbUser3.getPhone())) {
            phone = dbUser3.getPhone();
        }
        if (StringUtils.isEmpty(phone)) {
            return AjaxResult.error("\u8bf7\u5148\u7ed1\u5b9a\u624b\u673a\u53f7\u540e\u518d\u4f7f\u7528\u4f1a\u5458\u7801");
        }
        String nickname = sessionData.get("nickname");
        if (StringUtils.isEmpty(nickname) && this.wxMaUserMapper != null && (dbUser2 = this.wxMaUserMapper.selectByOpenid(openid)) != null && StringUtils.isNotEmpty(dbUser2.getNickname())) {
            nickname = dbUser2.getNickname();
        }
        if (StringUtils.isEmpty(avatarUrl = sessionData.get("avatarUrl")) && this.wxMaUserMapper != null && (dbUser = this.wxMaUserMapper.selectByOpenid(openid)) != null && StringUtils.isNotEmpty(dbUser.getAvatarUrl())) {
            avatarUrl = dbUser.getAvatarUrl();
        }
        if (this.umsMemberService == null) {
            return AjaxResult.error("\u4f1a\u5458\u670d\u52a1\u672a\u914d\u7f6e");
        }
        UmsMember member = this.umsMemberService.getByPhone(phone);
        if (member == null) {
            member = new UmsMember();
            member.setPhone(phone);
            member.setNickname((String)(StringUtils.isNotEmpty(nickname) ? nickname : "\u7528\u6237" + phone.substring(Math.max(0, phone.length() - 4))));
            member.setAvatar(avatarUrl);
            member.setPoints(0);
            member.setBalance(BigDecimal.ZERO);
            member.setLevel(0);
            member.setDelFlag("0");
            member.setCreateTime(LocalDateTime.now());
            member.setUpdateTime(LocalDateTime.now());
            this.umsMemberService.save(member);
        }
        if (member == null || member.getMemberCode() == null) {
            return AjaxResult.error("\u65e0\u6cd5\u751f\u6210\u4f1a\u5458\u7801");
        }
        DynamicCodeDTO dynamicCodeDTO = null;
        if (this.dynamicCodeService != null && StringUtils.isNotEmpty(member.getPhone())) {
            dynamicCodeDTO = this.dynamicCodeService.generateDynamicMemberCodeDTO(member.getId(), member.getMemberCode(), member.getPhone());
        }
        HashMap<String, Object> data = new HashMap<String, Object>();
        if (dynamicCodeDTO != null && StringUtils.isNotEmpty(dynamicCodeDTO.getEncryptedCode())) {
            data.put("memberCode", dynamicCodeDTO.getEncryptedCode());
            data.put("timestamp", dynamicCodeDTO.getTimestamp());
            data.put("dynamicCode", true);
            data.put("expireSeconds", dynamicCodeDTO.getExpireSeconds());
        } else {
            data.put("memberCode", this.maskMemberCode(member.getMemberCode()));
            data.put("dynamicCode", false);
        }
        data.put("nickname", member.getNickname());
        data.put("points", member.getPoints() != null ? member.getPoints() : 0);
        data.put("level", member.getLevel() != null ? member.getLevel() : 0);
        data.put("userId", String.valueOf(member.getId()));
        return AjaxResult.success(data);
    }

    private String maskMemberCode(String memberCode) {
        if (StringUtils.isEmpty(memberCode) || memberCode.length() < 4) {
            return memberCode;
        }
        int len = memberCode.length();
        return memberCode.substring(0, 2) + "****" + memberCode.substring(len - 2);
    }

    @Operation(summary="\u6211\u7684\u5546\u54c1\u5238", description="\u9700\u767b\u5f55\u4e14\u5df2\u7ed1\u5b9a\u624b\u673a\u53f7\uff1b\u8fd4\u56de\u5f53\u524d\u7528\u6237\u7684\u5546\u54c1\u5238\u5217\u8868")
    @GetMapping(value={"/coupons"})
    public AjaxResult getMyCoupons(HttpServletRequest request, @RequestParam(required=false) Integer status) {
        WxMaUser dbUser;
        String phone;
        String token = WxUserApi.resolveToken(request, null);
        Map<String, String> sessionData = this.getSessionDataByToken(token, request);
        if (sessionData == null) {
            return AjaxResult.error(WxUserApi.tokenErrorMsg(token));
        }
        String openid = sessionData.get("openid");
        if (StringUtils.isEmpty(openid)) {
            openid = sessionData.get("memberId");
        }
        if (StringUtils.isEmpty(phone = sessionData.get("phoneNumber"))) {
            phone = sessionData.get("phone");
        }
        if (StringUtils.isEmpty(phone) && this.wxMaUserMapper != null && (dbUser = this.wxMaUserMapper.selectByOpenid(openid)) != null && StringUtils.isNotEmpty(dbUser.getPhone())) {
            phone = dbUser.getPhone();
        }
        if (StringUtils.isEmpty(phone)) {
            return AjaxResult.success(Collections.emptyList());
        }
        if (this.umsMemberService == null || this.tbCouponInfoService == null) {
            return AjaxResult.success(Collections.emptyList());
        }
        UmsMember member = this.umsMemberService.getByPhone(phone);
        if (member == null) {
            return AjaxResult.success(Collections.emptyList());
        }
        List<TbCouponInfo> list = this.tbCouponInfoService.getUserCoupons(member.getId(), status);
        return AjaxResult.success(list);
    }

    @Operation(summary="\u6bcf\u65e5\u7b7e\u5230", description="\u9700\u767b\u5f55\u4e14\u5df2\u7ed1\u5b9a\u624b\u673a\u53f7\uff1b\u6bcf\u65e5\u9996\u6b21\u7b7e\u5230\u53ef\u83b7\u5f97\u79ef\u5206")
    @PostMapping(value={"/sign-in"})
    public AjaxResult signIn(HttpServletRequest request, @RequestBody(required=false) Map<String, String> body) {
        WxMaUser dbUser;
        String phone;
        String token = WxUserApi.resolveToken(request, body);
        Map<String, String> sessionData = this.getSessionDataByToken(token, request);
        if (sessionData == null) {
            return AjaxResult.error(WxUserApi.tokenErrorMsg(token));
        }
        String openid = sessionData.get("openid");
        if (StringUtils.isEmpty(openid)) {
            openid = sessionData.get("memberId");
        }
        if (StringUtils.isEmpty(phone = sessionData.get("phoneNumber"))) {
            phone = sessionData.get("phone");
        }
        if (StringUtils.isEmpty(phone) && this.wxMaUserMapper != null && (dbUser = this.wxMaUserMapper.selectByOpenid(openid)) != null && StringUtils.isNotEmpty(dbUser.getPhone())) {
            phone = dbUser.getPhone();
        }
        if (StringUtils.isEmpty(phone)) {
            return AjaxResult.error("\u8bf7\u5148\u7ed1\u5b9a\u624b\u673a\u53f7\u540e\u518d\u7b7e\u5230");
        }
        if (this.umsMemberService == null || this.tbIntegralRuleService == null) {
            return AjaxResult.error("\u7b7e\u5230\u529f\u80fd\u6682\u672a\u5f00\u653e");
        }
        UmsMember member = this.umsMemberService.getByPhone(phone);
        if (member == null) {
            return AjaxResult.error("\u8bf7\u5148\u7ed1\u5b9a\u624b\u673a\u53f7\u540e\u518d\u7b7e\u5230");
        }
        boolean success = this.tbIntegralRuleService.distributeSignInPoints(member.getId());
        if (success) {
            return AjaxResult.success("\u7b7e\u5230\u6210\u529f");
        }
        return AjaxResult.error("\u4eca\u65e5\u5df2\u7b7e\u5230\uff0c\u660e\u5929\u518d\u6765\u5427");
    }

    @Schema(description="\u5c0f\u7a0b\u5e8f\u7528\u6237\u4fe1\u606f")
    public static class WxUserInfoVO {
        @Schema(description="\u7528\u6237ID(openid)")
        public String userId;
        @Schema(description="\u6635\u79f0")
        public String nickname;
        @Schema(description="\u5934\u50cfURL")
        public String avatarUrl;
    }

    @Schema(description="\u5c0f\u7a0b\u5e8f\u767b\u5f55\u54cd\u5e94")
    public static class WxLoginVO {
        @Schema(description="\u8bbf\u95ee\u4ee4\u724c\uff0c\u540e\u7eed\u8bf7\u6c42 Header \u4f20 X-Wx-Token")
        public String token;
        @Schema(description="\u7528\u6237\u552f\u4e00\u6807\u8bc6(openid)")
        public String userId;
        @Schema(description="\u6635\u79f0\uff08\u82e5\u7528\u6237\u66fe\u4fdd\u5b58\u8fc7\u5219\u8fd4\u56de\uff09")
        public String nickname;
        @Schema(description="\u5934\u50cfURL\uff08\u82e5\u7528\u6237\u66fe\u4fdd\u5b58\u8fc7\u5219\u8fd4\u56de\uff09")
        public String avatarUrl;
    }
}

