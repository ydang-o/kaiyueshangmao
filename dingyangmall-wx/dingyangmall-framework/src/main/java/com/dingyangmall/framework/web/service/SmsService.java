package com.dingyangmall.framework.web.service;

import com.dingyangmall.common.constant.CacheConstants;
import com.dingyangmall.common.core.redis.RedisCache;
import com.dingyangmall.common.exception.user.CaptchaException;
import com.dingyangmall.common.exception.user.CaptchaExpireException;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.framework.config.TencentSmsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 短信服务：支持腾讯云短信与模拟发送（未配置腾讯云时仅写 Redis，便于开发）
 *
 * @author dingyangmall
 */
@Component
public class SmsService {

    private static final Logger log = LoggerFactory.getLogger(SmsService.class);

    private static final int EXPIRE_MINUTES = 5;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private TencentSmsProperties tencentSmsProperties;

    /**
     * 发送短信验证码
     * 若已配置腾讯云且 enabled=true 则走腾讯云；否则仅生成验证码写入 Redis（模拟，不真实发短信）
     *
     * @param phone 手机号，11 位
     */
    public void sendSmsCode(String phone) {
        String code = generateCode();
        String key = CacheConstants.SMS_CODE_KEY + phone;
        redisCache.setCacheObject(key, code, EXPIRE_MINUTES, TimeUnit.MINUTES);

        if (tencentSmsProperties.isEnabled() && StringUtils.isNotEmpty(tencentSmsProperties.getSecretId())) {
            sendByTencent(phone, code);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("【模拟短信】发送给 {} 的验证码已生成（未配置腾讯云或未启用）", phone);
            }
        }
    }

    private String generateCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    /**
     * 调用腾讯云短信 API 发送验证码
     */
    private void sendByTencent(String phone, String code) {
        try {
            com.tencentcloudapi.common.Credential cred =
                    new com.tencentcloudapi.common.Credential(
                            tencentSmsProperties.getSecretId(),
                            tencentSmsProperties.getSecretKey());
            com.tencentcloudapi.sms.v20210111.SmsClient client =
                    new com.tencentcloudapi.sms.v20210111.SmsClient(cred, tencentSmsProperties.getRegion());

            com.tencentcloudapi.sms.v20210111.models.SendSmsRequest req =
                    new com.tencentcloudapi.sms.v20210111.models.SendSmsRequest();
            req.setSmsSdkAppId(tencentSmsProperties.getSmsSdkAppId());
            req.setSignName(tencentSmsProperties.getSignName());
            req.setTemplateId(tencentSmsProperties.getTemplateId());
            // 手机号需带国家码 +86
            String fullPhone = phone.startsWith("+86") ? phone : "+86" + phone;
            req.setPhoneNumberSet(new String[]{fullPhone});
            req.setTemplateParamSet(new String[]{code});

            com.tencentcloudapi.sms.v20210111.models.SendSmsResponse resp = client.SendSms(req);
            com.tencentcloudapi.sms.v20210111.models.SendStatus[] statusSet = resp.getSendStatusSet();
            if (statusSet != null && statusSet.length > 0) {
                com.tencentcloudapi.sms.v20210111.models.SendStatus status = statusSet[0];
                if (!"Ok".equals(status.getCode())) {
                    log.warn("腾讯云短信发送失败 phone={} code={} message={}", phone, status.getCode(), status.getMessage());
                    throw new RuntimeException("短信发送失败：" + status.getMessage());
                }
            }
            log.debug("腾讯云短信已发送 phone={}", phone);
        } catch (Exception e) {
            log.error("腾讯云短信发送异常 phone={}", phone, e);
            throw new RuntimeException(StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : "短信发送失败");
        }
    }

    /**
     * 校验短信验证码（校验通过后删除，防止重复使用）
     *
     * @param phone 手机号
     * @param code  验证码
     */
    public void validateSmsCode(String phone, String code) {
        String key = CacheConstants.SMS_CODE_KEY + phone;
        Object cached = redisCache.getCacheObject(key);

        if (cached == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equals(String.valueOf(cached))) {
            throw new CaptchaException();
        }
        redisCache.deleteObject(key);
    }
}
