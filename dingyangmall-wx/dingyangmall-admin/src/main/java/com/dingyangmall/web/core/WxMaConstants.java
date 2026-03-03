package com.dingyangmall.web.core;

/**
 * 小程序接口统一常量：使用令牌（token）认证，请求头、Redis 键、body 字段等。
 */
public final class WxMaConstants {

    private WxMaConstants() {}

    /** 小程序令牌请求头名称（唯一标准），值为登录接口返回的 token，后续所有需登录态请求均带此头 */
    public static final String HEADER_TOKEN = "X-Wx-Token";

    /** Authorization 头 Bearer 前缀，兼容标准写法：Authorization: Bearer &lt;token&gt; */
    public static final String AUTHORIZATION_BEARER = "Bearer ";

    /** Redis 令牌键前缀，完整键为 wx:token:&lt;token&gt;，值为会话 Map（openid、sessionKey、memberId 等） */
    public static final String REDIS_TOKEN_PREFIX = "wx:token:";

    /** Body 兜底字段名（POST 时若未带请求头，可从此字段取 token） */
    public static final String BODY_TOKEN_KEY = "token";
}
