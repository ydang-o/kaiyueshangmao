/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.api;

import com.dingyangmall.common.core.domain.AjaxResult;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/wxmsg"})
public class WxMsgApi {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(WxMsgApi.class);

    @PostMapping(value={"/send"})
    public AjaxResult sendTemplateMsg(@RequestBody Object msgData) {
        return AjaxResult.success("\u6d88\u606f\u53d1\u9001\u6210\u529f");
    }

    @GetMapping(value={"/list"})
    public AjaxResult getMsgList() {
        return AjaxResult.success();
    }

    @Generated
    public WxMsgApi() {
    }
}

