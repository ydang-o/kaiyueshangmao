package com.dingyangmall.web.api;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.utils.StringUtils;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 小程序用户登录与信息（README: /weixin/api/ma/wxuser）
 * <p>登录流程遵循微信官方文档：小程序端 wx.login 获取临时凭证 code（有效期 5 分钟、仅能使用一次），
 * 后端调用 auth.code2Session 换取 openid、unionid（若已绑定开放平台）、session_key；
 * 后端生成自定义登录态 thirdSession 存 Redis，session_key 仅存服务端、绝不下发至前端。</p>
 * <ul>
 *   <li>POST /login：code2Session，返回 thirdSession、userId，若已有昵称/头像则一并返回</li>
 *   <li>GET /info：获取当前用户信息（userId、昵称、头像），需 Header third-session</li>
 *   <li>POST ""：更新用户信息（支持加密数据解密，或明文 nickname/avatarUrl）</li>
 * </ul>
 *
 * @author dingyangmall
 */
@RestController
@RequestMapping("/weixin/api/ma/wxuser")
public class WxUserApi {

    /** 仅用于文档：登录响应字段说明 */
    @Schema(description = "小程序登录响应")
    public static class WxLoginVO {
        @Schema(description = "第三方会话标识，后续请求需放在 Header third-session") public String thirdSession;
        @Schema(description = "用户ID(openid)") public String userId;
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
    private static final String REDIS_KEY_PREFIX = "wx:third_session:";
    private static final String REDIS_USER_INFO_PREFIX = "wx:user:info:";
    /** 已使用的 code 防重（微信规定 code 只能使用一次，有效期约 5 分钟） */
    private static final String REDIS_CODE_USED_PREFIX = "wx:code_used:";
    /** 同一 code 已创建过的 thirdSession（用于重复请求时幂等返回，符合「用户点击授权」可能触发的重试） */
    private static final String REDIS_CODE_TO_SESSION_PREFIX = "wx:code_to_session:";
    private static final long SESSION_EXPIRE_DAYS = 7;
    private static final long USER_INFO_EXPIRE_DAYS = 30;
    /** code 防重 key 保留时间（略大于微信 code 有效期 5 分钟） */
    private static final long CODE_USED_EXPIRE_MINUTES = 6;

    @Autowired(required = false)
    private WxMaService wxMaService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    private static String getThirdSession(HttpServletRequest request) {
        String v = request.getHeader("third-session");
        if (StringUtils.isEmpty(v)) {
            v = request.getHeader("Third-Session");
        }
        return v;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getSessionData(String thirdSession) {
        if (StringUtils.isEmpty(thirdSession)) {
            return null;
        }
        Object v = redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + thirdSession);
        return v instanceof Map ? (Map<String, String>) v : null;
    }

    @Operation(summary = "小程序登录", description = "微信官方流程：wx.login 获取 code（有效期 5 分钟、仅能使用一次），本接口调用 code2Session 换取 openid/session_key，生成自定义登录态 thirdSession；session_key 仅存服务端，绝不下发。")
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
        String codeToSessionKey = REDIS_CODE_TO_SESSION_PREFIX + code;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(codeUsedKey))) {
            Object existingSessionId = redisTemplate.opsForValue().get(codeToSessionKey);
            if (existingSessionId instanceof String) {
                String existingThird = (String) existingSessionId;
                Map<String, String> existingData = getSessionData(existingThird);
                if (existingData != null) {
                    AjaxResult ajax = AjaxResult.success();
                    ajax.put("thirdSession", existingThird);
                    ajax.put("userId", existingData.get("wxUserId") != null ? existingData.get("wxUserId") : existingData.get("openid"));
                    if (StringUtils.isNotEmpty(existingData.get("nickname"))) {
                        ajax.put("nickname", existingData.get("nickname"));
                    }
                    if (StringUtils.isNotEmpty(existingData.get("avatarUrl"))) {
                        ajax.put("avatarUrl", existingData.get("avatarUrl"));
                    }
                    return ajax;
                }
            }
            log.warn("登录凭证已使用且无法复用会话，请用户重新点击登录");
            return AjaxResult.error("该登录凭证已使用，请重新点击「微信一键登录」获取新凭证");
        }
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            String openid = session.getOpenid();
            String sessionKey = session.getSessionKey();
            if (StringUtils.isEmpty(openid)) {
                return AjaxResult.error("微信登录失败，openid 为空");
            }

            String thirdSession = UUID.randomUUID().toString().replace("-", "");
            String redisKey = REDIS_KEY_PREFIX + thirdSession;
            Map<String, String> sessionData = new HashMap<>();
            sessionData.put("openid", openid);
            sessionData.put("sessionKey", sessionKey);
            sessionData.put("wxUserId", openid);
            if (StringUtils.isNotEmpty(session.getUnionid())) {
                sessionData.put("unionid", session.getUnionid());
            }

            Object userInfoObj = redisTemplate.opsForValue().get(REDIS_USER_INFO_PREFIX + openid);
            if (userInfoObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, String> userInfo = (Map<String, String>) userInfoObj;
                String nickname = userInfo.get("nickname");
                String avatarUrl = userInfo.get("avatarUrl");
                if (StringUtils.isNotEmpty(nickname)) {
                    sessionData.put("nickname", nickname);
                }
                if (StringUtils.isNotEmpty(avatarUrl)) {
                    sessionData.put("avatarUrl", avatarUrl);
                }
            }

            redisTemplate.opsForValue().set(redisKey, sessionData, SESSION_EXPIRE_DAYS, TimeUnit.DAYS);
            redisTemplate.opsForValue().set(codeToSessionKey, thirdSession, CODE_USED_EXPIRE_MINUTES, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set(codeUsedKey, "1", CODE_USED_EXPIRE_MINUTES, TimeUnit.MINUTES);

            AjaxResult ajax = AjaxResult.success();
            ajax.put("thirdSession", thirdSession);
            ajax.put("userId", openid);
            @SuppressWarnings("unchecked")
            Map<String, String> info = (Map<String, String>) redisTemplate.opsForValue().get(REDIS_USER_INFO_PREFIX + openid);
            if (info != null) {
                if (StringUtils.isNotEmpty(info.get("nickname"))) {
                    ajax.put("nickname", info.get("nickname"));
                }
                if (StringUtils.isNotEmpty(info.get("avatarUrl"))) {
                    ajax.put("avatarUrl", info.get("avatarUrl"));
                }
            }
            return ajax;
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
            if (errCode == 40163) {
                Object existingSessionId = redisTemplate.opsForValue().get(codeToSessionKey);
                if (existingSessionId instanceof String) {
                    Map<String, String> existingData = getSessionData((String) existingSessionId);
                    if (existingData != null) {
                        AjaxResult ajax = AjaxResult.success();
                        ajax.put("thirdSession", existingSessionId);
                        ajax.put("userId", existingData.get("wxUserId") != null ? existingData.get("wxUserId") : existingData.get("openid"));
                        if (StringUtils.isNotEmpty(existingData.get("nickname"))) ajax.put("nickname", existingData.get("nickname"));
                        if (StringUtils.isNotEmpty(existingData.get("avatarUrl"))) ajax.put("avatarUrl", existingData.get("avatarUrl"));
                        return ajax;
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

    @Operation(summary = "获取当前用户信息", description = "返回 userId、昵称、头像；需在 Header 携带 third-session")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = WxUserInfoVO.class)))
    })
    @GetMapping("/info")
    public AjaxResult getUserInfo(HttpServletRequest request) {
        String thirdSession = getThirdSession(request);
        Map<String, String> sessionData = getSessionData(thirdSession);
        if (sessionData == null) {
            return AjaxResult.error("未登录或登录已过期");
        }
        String userId = sessionData.get("wxUserId");
        if (StringUtils.isEmpty(userId)) {
            userId = sessionData.get("openid");
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("userId", userId);
        if (StringUtils.isNotEmpty(sessionData.get("nickname"))) {
            ajax.put("nickname", sessionData.get("nickname"));
        }
        if (StringUtils.isNotEmpty(sessionData.get("avatarUrl"))) {
            ajax.put("avatarUrl", sessionData.get("avatarUrl"));
        }
        return ajax;
    }

    @Operation(summary = "更新用户信息", description = "支持 encryptedData+iv 解密或明文 nickname/avatarUrl；需在 Header 携带 third-session")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = WxUserInfoVO.class)))
    })
    @PostMapping
    public AjaxResult updateUser(HttpServletRequest request, @RequestBody Map<String, String> body) {
        if (body == null || body.isEmpty()) {
            return AjaxResult.error("参数不能为空");
        }
        String thirdSession = getThirdSession(request);
        Map<String, String> sessionData = getSessionData(thirdSession);
        if (sessionData == null) {
            return AjaxResult.error("未登录或登录已过期");
        }
        String openid = sessionData.get("openid");
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
            avatarUrl = body.get("avatarUrl");
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
        String redisKey = REDIS_KEY_PREFIX + thirdSession;
        redisTemplate.opsForValue().set(redisKey, sessionData, SESSION_EXPIRE_DAYS, TimeUnit.DAYS);

        @SuppressWarnings("unchecked")
        Map<String, String> userInfo = (Map<String, String>) redisTemplate.opsForValue().get(REDIS_USER_INFO_PREFIX + openid);
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
        if (!userInfo.isEmpty()) {
            redisTemplate.opsForValue().set(REDIS_USER_INFO_PREFIX + openid, userInfo, USER_INFO_EXPIRE_DAYS, TimeUnit.DAYS);
        }

        AjaxResult ajax = AjaxResult.success();
        ajax.put("userId", openid);
        if (StringUtils.isNotEmpty(nickname)) {
            ajax.put("nickname", nickname);
        }
        if (StringUtils.isNotEmpty(avatarUrl)) {
            ajax.put("avatarUrl", avatarUrl);
        }
        return ajax;
    }
}
