/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.api;

import com.dingyangmall.common.core.domain.AjaxResult;
import java.util.HashMap;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/wxsummary"})
public class WxSummaryApi {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(WxSummaryApi.class);

    @GetMapping(value={"/user"})
    public AjaxResult getUserSummary(@RequestParam(value="startDate", required=false) String startDate, @RequestParam(value="endDate", required=false) String endDate) {
        HashMap<String, Integer> data = new HashMap<String, Integer>();
        data.put("newUsers", 0);
        data.put("totalUsers", 0);
        data.put("activeUsers", 0);
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/msg"})
    public AjaxResult getMsgSummary(@RequestParam(value="startDate", required=false) String startDate, @RequestParam(value="endDate", required=false) String endDate) {
        HashMap<String, Integer> data = new HashMap<String, Integer>();
        data.put("sentMsgs", 0);
        data.put("receivedMsgs", 0);
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/interface"})
    public AjaxResult getInterfaceSummary(@RequestParam(value="startDate", required=false) String startDate, @RequestParam(value="endDate", required=false) String endDate) {
        HashMap<String, Integer> data = new HashMap<String, Integer>();
        data.put("apiCalls", 0);
        data.put("apiFails", 0);
        return AjaxResult.success(data);
    }

    @Generated
    public WxSummaryApi() {
    }
}

