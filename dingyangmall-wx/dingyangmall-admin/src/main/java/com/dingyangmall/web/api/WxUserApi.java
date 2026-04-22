package com.dingyangmall.web.api;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.common.utils.uuid.IdUtils;
import com.dingyangmall.mall.entity.TbCouponInfo;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.TbCouponInfoService;
import com.dingyangmall.mall.service.TbIntegralRuleService;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.web.core.WxMaConstants;
import com.dingyangmall.web.entity.WxMaUser;
import com.dingyangmall.web.mapper.WxMaUserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import me.chanjar.weixin.common.error.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 小程序用户登录与信息（README: /weixin/api/ma/wxuser）
 * <p>登录遵循微信 code2Session，签发 token 存 Redis；后续请求均带 Header X-Wx-Token。</p>
 * <ul>
 *   <li>POST /login：code2Session，签发 token 返回；后续请求带 X-Wx-Token</li>
 *   <li>GET /info：需 Header X-Wx-Token</li>
 *   <li>POST ""：更新用户信息，需 Header X-Wx-Token</li>
 * </ul>
 */
@RestController
@RequestMapping(value = { "/weixin/api/ma/wxuser", "/api/ma/wxuser", "/wxuser" })
public class WxUserApi {

    /** 仅用于文档：登录响应字段说明 */
    @Schema(description = "小程序登录响应")
    public static class WxLoginVO {
        @Schema(description = "访问令牌，后续请求 Header 传 X-Wx-Token") public String token;
        @Schema(description = "用户唯一标识(openid)") public String userId;
        @Schema(description = "昵称（若用户曾保存过则返回）") public String nickname;
        @Schema(description = "头像URL（若用户曾保存过则返回）") public String avatarUrl;
    }

    /** 仅用于文档：用户信息字段说明 */
    @Schema(description = "小程序用户信息")
    public static class WxUserInfoVO {
        @Schema(description = "用户ID(openid)") public String userId;
        @Schema(description = "昵称") public String nickname;
        @Schema(description = "头像URL") public String avatarUrl;
    }

    private static final Logger log = LoggerFactory.getLogger(WxUserApi.class);
    /** Redis 键：openid 对应会话数据（登录/code 复用时使用） */
    private static final String REDIS_OPENID_SESSION_PREFIX = "wx:openid_session:";
    /** openid -> token 映射，用于 code 复用时返回已有 token */
    private static final String REDIS_OPENID_TO_TOKEN_PREFIX = "wx:openid_to_token:";
    private static final String REDIS_USER_INFO_PREFIX = "wx:user:info:";
    /** 已使用的 code 防重（微信规定 code 只能使用一次，有效期约 5 分钟） */
    private static final String REDIS_CODE_USED_PREFIX = "wx:code_used:";
    /** 同一 code 已创建过的 openid（幂等返回） */
    private static final String REDIS_CODE_TO_OPENID_PREFIX = "wx:code_to_openid:";
    private static final long SESSION_EXPIRE_DAYS = 7;
    private static final long USER_INFO_EXPIRE_DAYS = 30;
    /** code 防重 key 保留时间（略大于微信 code 有效期 5 分钟） */
    private static final long CODE_USED_EXPIRE_MINUTES = 6;

    /** 微信 openid 格式：以 o 开头、长度约 28 */
    private static boolean isOpenidFormat(String s) {
        return StringUtils.isNotEmpty(s) && s.length() >= 26 && s.length() <= 32 && s.startsWith("o");
    }

    @Autowired(required = false)
    private WxMaService wxMaService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired(required = false)
    private WxMaUserMapper wxMaUserMapper;

    @Autowired(required = false)
    private UmsMemberService umsMemberService;

    @Autowired(required = false)
    private TbCouponInfoService tbCouponInfoService;

    @Autowired(required = false)
    private TbIntegralRuleService tbIntegralRuleService;

    /** false 时登录不写 Redis，仅 DB（见 application.yml wx.ma.session-use-redis） */
    @Value("${wx.ma.session-use-redis:true}")
    private boolean sessionUseRedis;

    /** 从请求头获取令牌：X-Wx-Token 或 Authorization: Bearer &lt;token&gt; */
    private static String getTokenFromHeaders(HttpServletRequest request) {
        String v = request.getHeader(WxMaConstants.HEADER_TOKEN);
        if (StringUtils.isNotEmpty(v)) return v.trim();
        String auth = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(auth) && auth.startsWith(WxMaConstants.AUTHORIZATION_BEARER)) {
            v = auth.substring(WxMaConstants.AUTHORIZATION_BEARER.length()).trim();
            if (StringUtils.isNotEmpty(v)) return v;
        }
        return null;
    }

    /** 从 body 获取 token（标准字段 token） */
    private static String getTokenFromBody(Map<String, String> body) {
        if (body == null) return null;
        String v = body.get(WxMaConstants.BODY_TOKEN_KEY);
        return StringUtils.isNotEmpty(v) ? v.trim() : null;
    }

    /** 统一解析 token（header 优先，body 兜底） */
    private static String resolveToken(HttpServletRequest request, Map<String, String> body) {
        String token = getTokenFromHeaders(request);
        if (StringUtils.isEmpty(token)) token = getTokenFromBody(body);
        return token;
    }

    private String getSessionRedisKey(String openid) {
        if (!isOpenidFormat(openid)) return null;
        return REDIS_OPENID_SESSION_PREFIX + openid;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getSessionData(String openid) {
        if (!isOpenidFormat(openid)) return null;
        if (!sessionUseRedis) {
            if (wxMaUserMapper == null) return null;
            WxMaUser db = wxMaUserMapper.selectByOpenid(openid);
            if (db == null) return null;
            Map<String, String> m = new HashMap<>();
            m.put("openid", openid);
            m.put("memberId", openid);
            if (StringUtils.isNotEmpty(db.getNickname())) m.put("nickname", db.getNickname());
            if (StringUtils.isNotEmpty(db.getAvatarUrl())) m.put("avatarUrl", db.getAvatarUrl());
            return m;
        }
        String key = getSessionRedisKey(openid);
        if (key == null) return null;
        Object v = redisTemplate.opsForValue().get(key);
        return v instanceof Map ? (Map<String, String>) v : null;
    }

    /** 按 token 从 Redis wx:token:{token} 获取会话数据 */
    @SuppressWarnings("unchecked")
    private Map<String, String> getSessionDataByToken(String token) {
        if (StringUtils.isEmpty(token)) return null;
        if (!sessionUseRedis) return null;
        String key = WxMaConstants.REDIS_TOKEN_PREFIX + token;
        Object v = redisTemplate.opsForValue().get(key);
        return v instanceof Map ? (Map<String, String>) v : null;
    }

    /** 构建登录成功响应体：返回 token 及 openid/userId 等，后续请求带 X-Wx-Token */
    private Map<String, Object> buildLoginResponseMap(String openid, Map<String, String> sessionData, String token) {
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", openid);
        data.put("openid", openid);
        if (sessionData != null) {
            if (StringUtils.isNotEmpty(sessionData.get("unionid"))) data.put("unionid", sessionData.get("unionid"));
            if (StringUtils.isNotEmpty(sessionData.get("nickname"))) data.put("nickname", sessionData.get("nickname"));
            if (StringUtils.isNotEmpty(sessionData.get("avatarUrl"))) data.put("avatarUrl", sessionData.get("avatarUrl"));
        }
        return data;
    }

    @Operation(summary = "小程序登录", description = "微信官方流程：wx.login 获取 code，本接口调用 code2Session 换取 openid/session_key，签发 token 存 Redis；返回 token，后续请求 Header 带 X-Wx-Token。")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = WxLoginVO.class)))
    })
    @PostMapping("/login")
    public AjaxResult login(@RequestBody Map<String, String> body) {
        String code = body != null ? (body.get("code") != null ? body.get("code") : body.get("jsCode")) : null;
        if (code != null) {
            code = code.trim();
        }
        if (StringUtils.isEmpty(code)) {
            return AjaxResult.error("缺少参数 code 或 jsCode（小程序需先调用 wx.login 获取 code）");
        }
        if (wxMaService == null) {
            log.warn("小程序登录失败：未配置 WxMaService，请检查 wx.ma.configs");
            return AjaxResult.error("小程序未配置，请联系管理员");
        }
        String codeUsedKey = REDIS_CODE_USED_PREFIX + code;
        String codeToOpenidKey = REDIS_CODE_TO_OPENID_PREFIX + code;
        if (sessionUseRedis && Boolean.TRUE.equals(redisTemplate.hasKey(codeUsedKey))) {
            Object existingOpenid = redisTemplate.opsForValue().get(codeToOpenidKey);
            if (existingOpenid instanceof String) {
                String openid = (String) existingOpenid;
                Map<String, String> existingData = getSessionData(openid);
                if (existingData != null) {
                    String existingToken = (String) redisTemplate.opsForValue().get(REDIS_OPENID_TO_TOKEN_PREFIX + openid);
                    if (StringUtils.isEmpty(existingToken)) {
                        existingToken = IdUtils.simpleUUID();
                        redisTemplate.opsForValue().set(WxMaConstants.REDIS_TOKEN_PREFIX + existingToken, existingData, SESSION_EXPIRE_DAYS, TimeUnit.DAYS);
                        redisTemplate.opsForValue().set(REDIS_OPENID_TO_TOKEN_PREFIX + openid, existingToken, SESSION_EXPIRE_DAYS, TimeUnit.DAYS);
                    }
                    return AjaxResult.success(buildLoginResponseMap(openid, existingData, existingToken));
                }
            }
            log.warn("登录凭证已使用且无法复用会话，请用户重新点击登录");
            return AjaxResult.error("该登录凭证已使用，请重新点击「微信一键登录」获取新凭证");
        }
        try {
            // 微信 code2Session 换取 openid、session_key，签发 token 存 Redis
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            String openid = session.getOpenid();
            String sessionKey = session.getSessionKey();
            if (StringUtils.isEmpty(openid)) {
                return AjaxResult.error("微信登录失败，openid 为空");
            }

            String redisKey = REDIS_OPENID_SESSION_PREFIX + openid;
            Map<String, String> sessionData = new HashMap<>();
            sessionData.put("openid", openid);
            sessionData.put("memberId", openid);
            sessionData.put("sessionKey", sessionKey);
            if (StringUtils.isNotEmpty(session.getUnionid())) {
                sessionData.put("unionid", session.getUnionid());
            }

            // 持久化 DB（使用旧表 wx_user）
            if (wxMaUserMapper != null) {
                try {
                    WxMaUser u = new WxMaUser();
                    u.setId(IdUtils.simpleUUID());
                    u.setOpenid(openid);
                    u.setUnionid(session.getUnionid());
                    wxMaUserMapper.upsert(u);
                } catch (Exception e) {
                    log.warn("wx_user 落库失败: {}", e.getMessage());
                }
            }

            if (sessionUseRedis) {
                Object userInfoObj = redisTemplate.opsForValue().get(REDIS_USER_INFO_PREFIX + openid);
                if (userInfoObj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, String> userInfo = (Map<String, String>) userInfoObj;
                    String nickname = userInfo.get("nickname");
                    String avatarUrl = userInfo.get("avatarUrl");
                    if (StringUtils.isNotEmpty(nickname)) sessionData.put("nickname", nickname);
                    if (StringUtils.isNotEmpty(avatarUrl)) sessionData.put("avatarUrl", avatarUrl);
                }
                redisTemplate.opsForValue().set(redisKey, sessionData, SESSION_EXPIRE_DAYS, TimeUnit.DAYS);
                String token = IdUtils.simpleUUID();
                redisTemplate.opsForValue().set(WxMaConstants.REDIS_TOKEN_PREFIX + token, sessionData, SESSION_EXPIRE_DAYS, TimeUnit.DAYS);
                redisTemplate.opsForValue().set(REDIS_OPENID_TO_TOKEN_PREFIX + openid, token, SESSION_EXPIRE_DAYS, TimeUnit.DAYS);
                redisTemplate.opsForValue().set(codeToOpenidKey, openid, CODE_USED_EXPIRE_MINUTES, TimeUnit.MINUTES);
                redisTemplate.opsForValue().set(codeUsedKey, "1", CODE_USED_EXPIRE_MINUTES, TimeUnit.MINUTES);
                if (log.isInfoEnabled()) {
                    log.info("[WxMa] 登录成功，已签发 token，Redis key={} expire={}天", WxMaConstants.REDIS_TOKEN_PREFIX + token, SESSION_EXPIRE_DAYS);
                }
            } else if (log.isInfoEnabled()) {
                log.info("[WxMa] 登录成功，已关闭 Redis，仅写 DB openid={}", openid != null ? openid.substring(0, Math.min(12, openid.length())) + "..." : "");
            }

            // 优先从数据库取昵称/头像，其次 Redis（Redis 关闭时仅 DB）
            if (wxMaUserMapper != null) {
                WxMaUser dbUser = wxMaUserMapper.selectByOpenid(openid);
                if (dbUser != null) {
                    if (StringUtils.isNotEmpty(dbUser.getNickname())) sessionData.put("nickname", dbUser.getNickname());
                    if (StringUtils.isNotEmpty(dbUser.getAvatarUrl())) sessionData.put("avatarUrl", dbUser.getAvatarUrl());
                }
            }
            if (sessionUseRedis && (!sessionData.containsKey("nickname") || !sessionData.containsKey("avatarUrl"))) {
                @SuppressWarnings("unchecked")
                Map<String, String> info = (Map<String, String>) redisTemplate.opsForValue().get(REDIS_USER_INFO_PREFIX + openid);
                if (info != null) {
                    if (!sessionData.containsKey("nickname") && StringUtils.isNotEmpty(info.get("nickname"))) sessionData.put("nickname", info.get("nickname"));
                    if (!sessionData.containsKey("avatarUrl") && StringUtils.isNotEmpty(info.get("avatarUrl"))) sessionData.put("avatarUrl", info.get("avatarUrl"));
                }
            }
            String token = sessionUseRedis ? (String) redisTemplate.opsForValue().get(REDIS_OPENID_TO_TOKEN_PREFIX + openid) : IdUtils.simpleUUID();
            if (StringUtils.isEmpty(token)) token = IdUtils.simpleUUID();
            return AjaxResult.success(buildLoginResponseMap(openid, sessionData, token));
        } catch (WxErrorException e) {
            log.warn("微信 code2Session 失败: errCode={}, errMsg={}", e.getError().getErrorCode(), e.getMessage());
            int errCode = e.getError().getErrorCode();
            if (errCode == 40013) {
                return AjaxResult.error("小程序 appId 未配置或错误，请在 application.yml 的 wx.ma.configs 中填写正确的小程序 AppID 和 AppSecret（微信公众平台-开发-开发管理）");
            }
            if (errCode == 40125) {
                return AjaxResult.error("小程序 appSecret 错误，请检查 wx.ma.configs 中的 secret 是否与微信公众平台一致");
            }
            if (errCode == 40029) {
                return AjaxResult.error("登录凭证无效或已过期，请重新打开小程序并调用 wx.login 后重试");
            }
            if (errCode == 40163 && sessionUseRedis) {
                Object existingOpenid = redisTemplate.opsForValue().get(codeToOpenidKey);
                if (existingOpenid instanceof String) {
                    String openid = (String) existingOpenid;
                    Map<String, String> existingData = getSessionData(openid);
                    if (existingData != null) {
                        String existingToken = (String) redisTemplate.opsForValue().get(REDIS_OPENID_TO_TOKEN_PREFIX + openid);
                        if (StringUtils.isEmpty(existingToken)) {
                            existingToken = IdUtils.simpleUUID();
                            redisTemplate.opsForValue().set(WxMaConstants.REDIS_TOKEN_PREFIX + existingToken, existingData, SESSION_EXPIRE_DAYS, TimeUnit.DAYS);
                            redisTemplate.opsForValue().set(REDIS_OPENID_TO_TOKEN_PREFIX + openid, existingToken, SESSION_EXPIRE_DAYS, TimeUnit.DAYS);
                        }
                        return AjaxResult.success(buildLoginResponseMap(openid, existingData, existingToken));
                    }
                }
                return AjaxResult.error("该登录凭证已被使用，请重新点击「微信一键登录」");
            }
            return AjaxResult.error("微信登录失败：" + (e.getError().getErrorMsg() != null ? e.getError().getErrorMsg() : e.getMessage()));
        } catch (RuntimeException e) {
            Throwable c = e;
            while (c != null) {
                String msg = c.getMessage() != null ? c.getMessage() : "";
                if (msg.contains("Connect timed out") || msg.contains("ConnectTimeout") || c.getClass().getSimpleName().contains("Timeout")) {
                    log.warn("连接微信服务器超时，请检查本机网络、DNS 或代理: {}", e.getMessage());
                    return AjaxResult.error("连接微信服务器超时，请检查服务器网络或配置代理后重试");
                }
                c = c.getCause();
            }
            throw e;
        }
    }

    @Operation(summary = "获取当前用户信息", description = "返回 userId、昵称、头像；需在 Header 携带 X-Wx-Token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = WxUserInfoVO.class)))
    })
    /** 令牌校验失败时返回可区分原因的错误信息 */
    private static String tokenErrorMsg(String token) {
        if (StringUtils.isEmpty(token)) {
            return "未登录或登录已过期（原因：请求未携带 Header " + WxMaConstants.HEADER_TOKEN + " 或 body.token）";
        }
        return "未登录或登录已过期（原因：令牌无效或已过期，请重新登录）";
    }

    @GetMapping("/info")
    public AjaxResult getUserInfo(HttpServletRequest request) {
        String token = resolveToken(request, null);
        Map<String, String> sessionData = getSessionDataByToken(token);
        if (sessionData == null) {
            return AjaxResult.error(tokenErrorMsg(token));
        }
        String openid = sessionData.get("openid");
        if (StringUtils.isEmpty(openid)) openid = sessionData.get("memberId");
        Map<String, Object> info = new HashMap<>();
        info.put("userId", openid);
        info.put("openid", openid);
        if (StringUtils.isNotEmpty(sessionData.get("nickname"))) info.put("nickname", sessionData.get("nickname"));
        if (StringUtils.isNotEmpty(sessionData.get("avatarUrl"))) info.put("avatarUrl", sessionData.get("avatarUrl"));
        String phone = sessionData.get("phoneNumber");
        if (StringUtils.isEmpty(phone)) phone = sessionData.get("phone");
        if (StringUtils.isEmpty(phone) && wxMaUserMapper != null) {
            WxMaUser dbUser = wxMaUserMapper.selectByOpenid(openid);
            if (dbUser != null && StringUtils.isNotEmpty(dbUser.getPhone())) phone = dbUser.getPhone();
        }
        if (StringUtils.isNotEmpty(phone)) {
            info.put("phoneNumber", phone);
            info.put("phone", phone);
        }
        return AjaxResult.success(info);
    }

    @Operation(summary = "更新用户信息", description = "支持 encryptedData+iv 解密或明文 nickname/avatarUrl；需在 Header 携带 X-Wx-Token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = WxUserInfoVO.class)))
    })
    @PostMapping
    public AjaxResult updateUser(HttpServletRequest request, @RequestBody Map<String, String> body) {
        if (body == null || body.isEmpty()) {
            return AjaxResult.error("参数不能为空");
        }
        String token = resolveToken(request, body);
        Map<String, String> sessionData = getSessionDataByToken(token);
        if (sessionData == null) {
            return AjaxResult.error(tokenErrorMsg(token));
        }
        String openid = sessionData.get("openid");
        if (StringUtils.isEmpty(openid)) openid = sessionData.get("memberId");
        String sessionKey = sessionData.get("sessionKey");
        String nickname = null;
        String avatarUrl = null;

        String encryptedData = body.get("encryptedData");
        String iv = body.get("iv");
        if (StringUtils.isNotEmpty(encryptedData) && StringUtils.isNotEmpty(iv) && StringUtils.isNotEmpty(sessionKey) && wxMaService != null) {
            try {
                WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
                if (userInfo != null) {
                    nickname = userInfo.getNickName();
                    avatarUrl = userInfo.getAvatarUrl();
                }
            } catch (Exception e) {
                log.warn("解密用户信息失败: {}", e.getMessage());
                return AjaxResult.error("解密用户信息失败");
            }
        }
        if (nickname == null && avatarUrl == null) {
            nickname = body.get("nickname");
            if (StringUtils.isEmpty(nickname)) nickname = body.get("nickName");
            avatarUrl = body.get("avatarUrl");
        }
        // 兼容 uni.getUserProfile 直接把 detail 传给后端：nickname/avatar 可能在 userInfo 内
        Object ui = body.get("userInfo");
        if ((StringUtils.isEmpty(nickname) || StringUtils.isEmpty(avatarUrl)) && ui instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> userInfoMap = (Map<String, Object>) ui;
            if (StringUtils.isEmpty(nickname)) {
                Object nn = userInfoMap.get("nickName");
                if (nn == null) nn = userInfoMap.get("nickname");
                if (nn != null) nickname = String.valueOf(nn);
            }
            if (StringUtils.isEmpty(avatarUrl)) {
                Object av = userInfoMap.get("avatarUrl");
                if (av == null) av = userInfoMap.get("headimgUrl");
                if (av == null) av = userInfoMap.get("avatar");
                if (av != null) avatarUrl = String.valueOf(av);
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
        if (sessionUseRedis) {
            String redisKey = getSessionRedisKey(openid);
            if (redisKey != null) {
                redisTemplate.opsForValue().set(redisKey, sessionData, SESSION_EXPIRE_DAYS, TimeUnit.DAYS);
            }
            if (StringUtils.isNotEmpty(token)) {
                redisTemplate.opsForValue().set(WxMaConstants.REDIS_TOKEN_PREFIX + token, sessionData, SESSION_EXPIRE_DAYS, TimeUnit.DAYS);
            }
        }

        @SuppressWarnings("unchecked")
        Map<String, String> userInfo = sessionUseRedis
            ? (Map<String, String>) redisTemplate.opsForValue().get(REDIS_USER_INFO_PREFIX + openid)
            : null;
        if (userInfo == null) {
            userInfo = new HashMap<>();
        } else {
            userInfo = new HashMap<>(userInfo);
        }
        if (StringUtils.isNotEmpty(nickname)) {
            userInfo.put("nickname", nickname);
        }
        if (StringUtils.isNotEmpty(avatarUrl)) {
            userInfo.put("avatarUrl", avatarUrl);
        }
        if (sessionUseRedis && !userInfo.isEmpty()) {
            redisTemplate.opsForValue().set(REDIS_USER_INFO_PREFIX + openid, userInfo, USER_INFO_EXPIRE_DAYS, TimeUnit.DAYS);
        }

        // 持久化到数据库（使用旧表 wx_user，open_id 维度 upsert）
        if (wxMaUserMapper != null) {
            WxMaUser u = new WxMaUser();
            u.setId(IdUtils.simpleUUID());
            u.setOpenid(openid);
            if (StringUtils.isNotEmpty(sessionData.get("unionid"))) u.setUnionid(sessionData.get("unionid"));
            if (StringUtils.isNotEmpty(nickname)) u.setNickname(nickname);
            if (StringUtils.isNotEmpty(avatarUrl)) u.setAvatarUrl(avatarUrl);
            wxMaUserMapper.upsert(u);
        }

        Map<String, Object> result = new HashMap<>();
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

    @Operation(summary = "获取手机号", description = "前端 getPhoneNumber 回调中的 code 传到此接口，后端调微信 getuserphonenumber 解密；需登录态(Header X-Wx-Token)且小程序已开通手机号能力并企业认证。")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功，返回 phoneNumber、purePhoneNumber 等"),
        @ApiResponse(responseCode = "400", description = "code 无效或未开通手机号能力")
    })
    @PostMapping("/phone")
    public AjaxResult getPhoneNumber(HttpServletRequest request, @RequestBody Map<String, String> body) {
        String code = body != null ? body.get("code") : null;
        if (code != null) code = code.trim();
        if (StringUtils.isEmpty(code)) {
            return AjaxResult.error("缺少参数 code（需前端 getPhoneNumber 回调传入）");
        }
        String token = resolveToken(request, body);
        Map<String, String> sessionData = getSessionDataByToken(token);
        if (sessionData == null) {
            return AjaxResult.error(tokenErrorMsg(token));
        }
        if (wxMaService == null) {
            return AjaxResult.error("小程序未配置");
        }
        try {
            WxMaPhoneNumberInfo phoneInfo = wxMaService.getUserService().getPhoneNumber(code);
            if (phoneInfo == null) {
                return AjaxResult.error("获取手机号失败");
            }
            String phoneNumber = phoneInfo.getPhoneNumber();
            String purePhoneNumber = phoneInfo.getPurePhoneNumber();
            if (StringUtils.isEmpty(phoneNumber)) phoneNumber = purePhoneNumber;
            Map<String, Object> data = new HashMap<>();
            data.put("phoneNumber", phoneNumber);
            data.put("purePhoneNumber", purePhoneNumber);
            data.put("countryCode", phoneInfo.getCountryCode());
            String openid = sessionData.get("openid");
            if (StringUtils.isEmpty(openid)) openid = sessionData.get("memberId");
            if (StringUtils.isNotEmpty(openid) && StringUtils.isNotEmpty(phoneNumber)) {
                sessionData.put("phoneNumber", phoneNumber);
                sessionData.put("memberId", openid);
                sessionData.put("openid", openid);
                if (sessionUseRedis) {
                    String redisKey = getSessionRedisKey(openid);
                    if (redisKey != null) {
                        redisTemplate.opsForValue().set(redisKey, sessionData, SESSION_EXPIRE_DAYS, TimeUnit.DAYS);
                    }
                    if (StringUtils.isNotEmpty(token)) {
                        redisTemplate.opsForValue().set(WxMaConstants.REDIS_TOKEN_PREFIX + token, sessionData, SESSION_EXPIRE_DAYS, TimeUnit.DAYS);
                    }
                }
                if (wxMaUserMapper != null) {
                    try {
                        wxMaUserMapper.updatePhoneByOpenid(openid, phoneNumber);
                    } catch (Exception ex) {
                        log.warn("更新 wx_user 手机号失败: openid={}, {}", openid, ex.getMessage());
                    }
                }
            }
            return AjaxResult.success(data);
        } catch (WxErrorException e) {
            log.warn("获取手机号失败: errCode={}, errMsg={}", e.getError().getErrorCode(), e.getError().getErrorMsg());
            String msg = e.getError().getErrorMsg();
            if (msg != null && (msg.contains("phone") || msg.contains("权限") || msg.contains("能力"))) {
                return AjaxResult.error("未开通手机号能力或未企业认证，请在微信公众平台开通");
            }
            return AjaxResult.error(msg != null ? msg : "获取手机号失败");
        }
    }

    /**
     * 获取或生成当前用户的会员码（用于出示给商家扫码）。
     * 需先绑定手机号；若 ums_member 中无该手机号则自动创建并生成会员码。
     * 商家端扫会员码或输入会员码可识别用户并赠送积分等。
     */
    @Operation(summary = "获取会员码", description = "需登录且已绑定手机号；返回会员码及简要信息，用于出示给商家扫码")
    @GetMapping("/member-code")
    public AjaxResult getMemberCode(HttpServletRequest request) {
        String token = resolveToken(request, null);
        Map<String, String> sessionData = getSessionDataByToken(token);
        if (sessionData == null) {
            return AjaxResult.error(tokenErrorMsg(token));
        }
        String openid = sessionData.get("openid");
        if (StringUtils.isEmpty(openid)) openid = sessionData.get("memberId");
        String phone = sessionData.get("phoneNumber");
        if (StringUtils.isEmpty(phone)) phone = sessionData.get("phone");
        if (StringUtils.isEmpty(phone) && wxMaUserMapper != null) {
            WxMaUser dbUser = wxMaUserMapper.selectByOpenid(openid);
            if (dbUser != null && StringUtils.isNotEmpty(dbUser.getPhone())) {
                phone = dbUser.getPhone();
            }
        }
        if (StringUtils.isEmpty(phone)) {
            return AjaxResult.error("请先绑定手机号后再使用会员码");
        }
        String nickname = sessionData.get("nickname");
        if (StringUtils.isEmpty(nickname) && wxMaUserMapper != null) {
            WxMaUser dbUser = wxMaUserMapper.selectByOpenid(openid);
            if (dbUser != null && StringUtils.isNotEmpty(dbUser.getNickname())) {
                nickname = dbUser.getNickname();
            }
        }
        String avatarUrl = sessionData.get("avatarUrl");
        if (StringUtils.isEmpty(avatarUrl) && wxMaUserMapper != null) {
            WxMaUser dbUser = wxMaUserMapper.selectByOpenid(openid);
            if (dbUser != null && StringUtils.isNotEmpty(dbUser.getAvatarUrl())) {
                avatarUrl = dbUser.getAvatarUrl();
            }
        }
        if (umsMemberService == null) {
            return AjaxResult.error("会员服务未配置");
        }
        UmsMember member = umsMemberService.getByPhone(phone);
        if (member == null) {
            member = new UmsMember();
            member.setPhone(phone);
            member.setNickname(StringUtils.isNotEmpty(nickname) ? nickname : ("用户" + phone.substring(Math.max(0, phone.length() - 4))));
            member.setAvatar(avatarUrl);
            member.setPoints(0);
            member.setBalance(BigDecimal.ZERO);
            member.setLevel(0);
            member.setDelFlag("0");
            member.setCreateTime(LocalDateTime.now());
            member.setUpdateTime(LocalDateTime.now());
            umsMemberService.save(member);
        }
        if (member == null || member.getMemberCode() == null) {
            return AjaxResult.error("无法生成会员码");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("memberCode", member.getMemberCode());
        data.put("nickname", member.getNickname());
        data.put("points", member.getPoints() != null ? member.getPoints() : 0);
        data.put("level", member.getLevel() != null ? member.getLevel() : 0);
        return AjaxResult.success(data);
    }

    /**
     * 我的商品券（小程序用 token 解析出会员后查询 tb_coupon_info）
     * @param status 可选，1-未使用 2-已使用 3-已过期，不传返回全部
     */
    @Operation(summary = "我的商品券", description = "需登录且已绑定手机号；返回当前用户的商品券列表")
    @GetMapping("/coupons")
    public AjaxResult getMyCoupons(HttpServletRequest request, @RequestParam(required = false) Integer status) {
        String token = resolveToken(request, null);
        Map<String, String> sessionData = getSessionDataByToken(token);
        if (sessionData == null) {
            return AjaxResult.error(tokenErrorMsg(token));
        }
        String openid = sessionData.get("openid");
        if (StringUtils.isEmpty(openid)) openid = sessionData.get("memberId");
        String phone = sessionData.get("phoneNumber");
        if (StringUtils.isEmpty(phone)) phone = sessionData.get("phone");
        if (StringUtils.isEmpty(phone) && wxMaUserMapper != null) {
            WxMaUser dbUser = wxMaUserMapper.selectByOpenid(openid);
            if (dbUser != null && StringUtils.isNotEmpty(dbUser.getPhone())) phone = dbUser.getPhone();
        }
        if (StringUtils.isEmpty(phone)) {
            return AjaxResult.success(java.util.Collections.emptyList());
        }
        if (umsMemberService == null || tbCouponInfoService == null) {
            return AjaxResult.success(java.util.Collections.emptyList());
        }
        UmsMember member = umsMemberService.getByPhone(phone);
        if (member == null) {
            return AjaxResult.success(java.util.Collections.emptyList());
        }
        List<TbCouponInfo> list = tbCouponInfoService.getUserCoupons(member.getId(), status);
        return AjaxResult.success(list);
    }

    /**
     * 每日签到领积分（小程序用 token 解析出会员后调用签到逻辑）
     * 需先绑定手机号；openid → wx_user.phone → ums_member
     */
    @Operation(summary = "每日签到", description = "需登录且已绑定手机号；每日首次签到可获得积分")
    @PostMapping("/sign-in")
    public AjaxResult signIn(HttpServletRequest request, @RequestBody(required = false) Map<String, String> body) {
        String token = resolveToken(request, body);
        Map<String, String> sessionData = getSessionDataByToken(token);
        if (sessionData == null) {
            return AjaxResult.error(tokenErrorMsg(token));
        }
        String openid = sessionData.get("openid");
        if (StringUtils.isEmpty(openid)) openid = sessionData.get("memberId");
        String phone = sessionData.get("phoneNumber");
        if (StringUtils.isEmpty(phone)) phone = sessionData.get("phone");
        if (StringUtils.isEmpty(phone) && wxMaUserMapper != null) {
            WxMaUser dbUser = wxMaUserMapper.selectByOpenid(openid);
            if (dbUser != null && StringUtils.isNotEmpty(dbUser.getPhone())) phone = dbUser.getPhone();
        }
        if (StringUtils.isEmpty(phone)) {
            return AjaxResult.error("请先绑定手机号后再签到");
        }
        if (umsMemberService == null || tbIntegralRuleService == null) {
            return AjaxResult.error("签到功能暂未开放");
        }
        UmsMember member = umsMemberService.getByPhone(phone);
        if (member == null) {
            return AjaxResult.error("请先绑定手机号后再签到");
        }
        boolean success = tbIntegralRuleService.distributeSignInPoints(member.getId());
        if (success) {
            return AjaxResult.success("签到成功");
        } else {
            return AjaxResult.error("今日已签到，明天再来吧");
        }
    }
}
