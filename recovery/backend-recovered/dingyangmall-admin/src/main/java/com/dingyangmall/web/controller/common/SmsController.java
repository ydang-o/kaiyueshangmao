/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.controller.common;

import com.dingyangmall.common.annotation.RepeatSubmit;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.framework.web.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/common/sms"})
public class SmsController
extends BaseController {
    @Autowired
    private SmsService smsService;

    @RepeatSubmit(interval=60000, message="\u8bf760\u79d2\u540e\u518d\u8bd5")
    @GetMapping(value={"/send"})
    public AjaxResult sendSmsCode(@RequestParam(value="phone") String phone) {
        if (phone == null || phone.length() != 11) {
            return AjaxResult.error("\u624b\u673a\u53f7\u683c\u5f0f\u4e0d\u6b63\u786e");
        }
        this.smsService.sendSmsCode(phone);
        return AjaxResult.success("\u9a8c\u8bc1\u7801\u53d1\u9001\u6210\u529f");
    }
}

