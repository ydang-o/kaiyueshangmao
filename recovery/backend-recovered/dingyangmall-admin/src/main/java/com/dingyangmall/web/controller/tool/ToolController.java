/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.controller.tool;

import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import java.util.HashMap;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/tool", "/dev-api/tool", "/prod-api/tool"})
public class ToolController
extends BaseController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ToolController.class);

    @GetMapping(value={"/test"})
    public AjaxResult test() {
        return AjaxResult.success("\u8fde\u63a5\u6210\u529f");
    }

    @GetMapping(value={"/serverTime"})
    public AjaxResult getServerTime() {
        HashMap<String, Long> data = new HashMap<String, Long>();
        data.put("serverTime", System.currentTimeMillis());
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/health"})
    public AjaxResult health() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("status", "UP");
        data.put("timestamp", System.currentTimeMillis());
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/sysInfo"})
    public AjaxResult getSysInfo() {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("javaVersion", System.getProperty("java.version"));
        data.put("osName", System.getProperty("os.name"));
        data.put("osVersion", System.getProperty("os.version"));
        return AjaxResult.success(data);
    }
}

