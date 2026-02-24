package com.dingyangmall.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 腾讯云短信配置
 * 在腾讯云控制台：短信 -> 应用管理 -> 应用列表 获取 SdkAppId；访问管理 CAM 获取 SecretId/SecretKey
 * 签名与模板在 短信 -> 国内短信 -> 签名管理/正文模板管理 中创建
 *
 * @author dingyangmall
 */
@Data
@Component
@ConfigurationProperties(prefix = "tencent.sms")
public class TencentSmsProperties {

    /** 是否启用（未配置或 false 时使用模拟发送，仅写 Redis 不真实发短信） */
    private boolean enabled = false;

    /** 腾讯云 SecretId */
    private String secretId = "";

    /** 腾讯云 SecretKey */
    private String secretKey = "";

    /** 短信 SdkAppId（控制台 应用列表） */
    private String smsSdkAppId = "";

    /** 短信签名内容（需与控制台审核通过的一致） */
    private String signName = "";

    /** 验证码模板 ID（模板内容如：您的验证码为：{1}，5分钟内有效。） */
    private String templateId = "";

    /** 地域，默认 ap-guangzhou */
    private String region = "ap-guangzhou";
}
