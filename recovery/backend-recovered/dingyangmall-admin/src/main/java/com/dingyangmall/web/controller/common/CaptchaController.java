/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.controller.common;

import com.dingyangmall.common.config.DingyangmallConfig;
import com.dingyangmall.common.constant.Constants;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.redis.RedisCache;
import com.dingyangmall.common.utils.sign.Base64;
import com.dingyangmall.common.utils.uuid.IdUtils;
import com.dingyangmall.system.service.ISysConfigService;
import com.google.code.kaptcha.Producer;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CaptchaController {
    @Resource(name="captchaProducer")
    private Producer captchaProducer;
    @Resource(name="captchaProducerMath")
    private Producer captchaProducerMath;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ISysConfigService configService;

    @GetMapping(value={"/captchaImage"})
    public AjaxResult getCode(HttpServletResponse response) throws IOException {
        AjaxResult ajax = AjaxResult.success();
        boolean captchaEnabled = this.configService.selectCaptchaEnabled();
        ajax.put("captchaEnabled", (Object)captchaEnabled);
        if (!captchaEnabled) {
            return ajax;
        }
        String uuid = IdUtils.simpleUUID();
        String verifyKey = "captcha_codes:" + uuid;
        String capStr = null;
        String code = null;
        BufferedImage image = null;
        String captchaType = DingyangmallConfig.getCaptchaType();
        if ("math".equals(captchaType)) {
            String capText = this.captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = this.captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = this.captchaProducer.createText();
            image = this.captchaProducer.createImage(capStr);
        }
        this.redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write((RenderedImage)image, "jpg", os);
        }
        catch (IOException e) {
            return AjaxResult.error(e.getMessage());
        }
        ajax.put("uuid", (Object)uuid);
        ajax.put("img", (Object)Base64.encode(os.toByteArray()));
        return ajax;
    }
}

