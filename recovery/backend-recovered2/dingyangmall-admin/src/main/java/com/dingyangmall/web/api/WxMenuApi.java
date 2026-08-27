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
@RequestMapping(value={"/wxmenu"})
public class WxMenuApi {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(WxMenuApi.class);

    @PostMapping(value={"/create"})
    public AjaxResult createMenu(@RequestBody Object menuData) {
        return AjaxResult.success("\u83dc\u5355\u521b\u5efa\u6210\u529f");
    }

    @GetMapping(value={"/get"})
    public AjaxResult getMenu() {
        return AjaxResult.success();
    }

    @PostMapping(value={"/delete"})
    public AjaxResult deleteMenu() {
        return AjaxResult.success("\u83dc\u5355\u5220\u9664\u6210\u529f");
    }

    @Generated
    public WxMenuApi() {
    }
}

