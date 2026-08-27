/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.web.service;

import com.dingyangmall.common.core.redis.RedisCache;
import com.dingyangmall.common.exception.user.CaptchaException;
import com.dingyangmall.common.exception.user.CaptchaExpireException;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.framework.config.TencentSmsProperties;
import com.dingyangmall.framework.config.VolcSmsProperties;
import com.dingyangmall.framework.web.service.VolcSmsService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SmsService {
    private static final Logger log = LoggerFactory.getLogger(SmsService.class);
    private static final int EXPIRE_MINUTES = 5;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private TencentSmsProperties tencentSmsProperties;
    @Autowired
    private VolcSmsProperties volcSmsProperties;
    @Autowired
    private VolcSmsService volcSmsService;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public void sendSmsCode(String phone) {
        String code = this.generateCode();
        String key = "sms_codes:" + phone;
        this.redisCache.setCacheObject(key, code, 5, TimeUnit.MINUTES);
        log.debug("\u9a8c\u8bc1\u7801\u5df2\u751f\u6210\u5e76\u5b58\u50a8 phone={}", (Object)phone);
        if (this.volcSmsService.isConfigured()) {
            this.sendByVolc(phone, code);
        } else if (this.tencentSmsProperties.isEnabled() && StringUtils.isNotEmpty(this.tencentSmsProperties.getSecretId())) {
            this.sendByTencent(phone, code);
        } else if (log.isDebugEnabled()) {
            log.debug("\u3010\u6a21\u62df\u77ed\u4fe1\u3011\u53d1\u9001\u7ed9 {} \u7684\u9a8c\u8bc1\u7801\u5df2\u751f\u6210\uff08\u672a\u914d\u7f6e\u77ed\u4fe1\u670d\u52a1\u6216\u672a\u542f\u7528\uff09", (Object)phone);
        }
    }

    private String generateCode() {
        int code = 100000 + SECURE_RANDOM.nextInt(900000);
        return String.valueOf(code);
    }

    private void sendByVolc(String phone, String code) {
        VolcSmsService.SmsSendResult result = this.volcSmsService.sendSmsCode(phone, code);
        if (!result.isSuccess()) {
            log.error("\u706b\u5c71\u5f15\u64ce\u77ed\u4fe1\u53d1\u9001\u5931\u8d25 phone={} error={}", (Object)phone, (Object)result.getError());
            throw new RuntimeException(StringUtils.isNotEmpty(result.getError()) ? result.getError() : "\u77ed\u4fe1\u53d1\u9001\u5931\u8d25");
        }
        log.debug("\u706b\u5c71\u5f15\u64ce\u77ed\u4fe1\u5df2\u53d1\u9001 phone={} messageId={}", (Object)phone, (Object)result.getMessageId());
    }

    private void sendByTencent(String phone, String code) {
        try {
            SendStatus status;
            Credential cred = new Credential(this.tencentSmsProperties.getSecretId(), this.tencentSmsProperties.getSecretKey());
            SmsClient client = new SmsClient(cred, this.tencentSmsProperties.getRegion());
            SendSmsRequest req = new SendSmsRequest();
            req.setSmsSdkAppId(this.tencentSmsProperties.getSmsSdkAppId());
            req.setSignName(this.tencentSmsProperties.getSignName());
            req.setTemplateId(this.tencentSmsProperties.getTemplateId());
            Object fullPhone = phone.startsWith("+86") ? phone : "+86" + phone;
            req.setPhoneNumberSet(new String[]{fullPhone});
            req.setTemplateParamSet(new String[]{code});
            SendSmsResponse resp = client.SendSms(req);
            SendStatus[] statusSet = resp.getSendStatusSet();
            if (statusSet != null && statusSet.length > 0 && !"Ok".equals((status = statusSet[0]).getCode())) {
                log.warn("\u817e\u8baf\u4e91\u77ed\u4fe1\u53d1\u9001\u5931\u8d25 phone={} code={} message={}", phone, status.getCode(), status.getMessage());
                throw new RuntimeException("\u77ed\u4fe1\u53d1\u9001\u5931\u8d25\uff1a" + status.getMessage());
            }
            log.debug("\u817e\u8baf\u4e91\u77ed\u4fe1\u5df2\u53d1\u9001 phone={}", (Object)phone);
        }
        catch (Exception e) {
            log.error("\u817e\u8baf\u4e91\u77ed\u4fe1\u53d1\u9001\u5f02\u5e38 phone={}", (Object)phone, (Object)e);
            throw new RuntimeException(StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : "\u77ed\u4fe1\u53d1\u9001\u5931\u8d25");
        }
    }

    public void validateSmsCode(String phone, String code) {
        String key = "sms_codes:" + phone;
        Object cached = this.redisCache.getCacheObject(key);
        log.debug("\u9a8c\u8bc1\u7801\u6821\u9a8c phone={}", (Object)phone);
        if (cached != null) {
            String cachedStr = String.valueOf(cached);
            if (!code.equals(cachedStr)) {
                log.warn("\u9a8c\u8bc1\u7801\u4e0d\u5339\u914d phone={}", (Object)phone);
                throw new CaptchaException();
            }
            this.redisCache.deleteObject(key);
            log.debug("\u9a8c\u8bc1\u7801\u6821\u9a8c\u6210\u529f phone={}", (Object)phone);
            return;
        }
        if (this.volcSmsService.isConfigured()) {
            log.info("\u672c\u5730\u65e0\u7f13\u5b58\uff0c\u5c1d\u8bd5\u706b\u5c71\u5f15\u64ce\u6821\u9a8c phone={}", (Object)phone);
            boolean valid = this.volcSmsService.checkVerifyCode(phone, code);
            if (!valid) {
                throw new CaptchaException();
            }
            return;
        }
        log.warn("\u9a8c\u8bc1\u7801\u5df2\u8fc7\u671f phone={}", (Object)phone);
        throw new CaptchaExpireException();
    }
}

