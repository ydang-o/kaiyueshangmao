/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 *  org.springframework.web.multipart.MultipartFile
 */
package com.dingyangmall.web.api;

import com.dingyangmall.common.core.domain.AjaxResult;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value={"/wxmaterial"})
public class WxMaterialApi {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(WxMaterialApi.class);

    @PostMapping(value={"/upload"})
    public AjaxResult uploadMaterial(@RequestParam(value="file") MultipartFile file, @RequestParam(value="type") String type) {
        return AjaxResult.success((String)"\u7d20\u6750\u4e0a\u4f20\u6210\u529f");
    }

    @GetMapping(value={"/list"})
    public AjaxResult getMaterialList(@RequestParam(value="type", defaultValue="image") String type, @RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="size", defaultValue="10") int size) {
        return AjaxResult.success();
    }

    @PostMapping(value={"/delete/{mediaId}"})
    public AjaxResult deleteMaterial(@PathVariable String mediaId) {
        return AjaxResult.success((String)"\u7d20\u6750\u5220\u9664\u6210\u529f");
    }

    @Generated
    public WxMaterialApi() {
    }
}

