/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.api;

import com.dingyangmall.common.core.domain.AjaxResult;
import java.util.ArrayList;
import java.util.Map;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/wxusertags"})
public class WxUserTagsApi {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(WxUserTagsApi.class);

    @GetMapping(value={"/list"})
    public AjaxResult getTagsList() {
        ArrayList tags = new ArrayList();
        return AjaxResult.success(tags);
    }

    @PostMapping(value={"/create"})
    public AjaxResult createTag(@RequestBody Map<String, Object> tagData) {
        return AjaxResult.success("\u6807\u7b7e\u521b\u5efa\u6210\u529f");
    }

    @PostMapping(value={"/update/{tagId}"})
    public AjaxResult updateTag(@PathVariable Long tagId, @RequestBody Map<String, Object> tagData) {
        return AjaxResult.success("\u6807\u7b7e\u66f4\u65b0\u6210\u529f");
    }

    @PostMapping(value={"/delete/{tagId}"})
    public AjaxResult deleteTag(@PathVariable Long tagId) {
        return AjaxResult.success("\u6807\u7b7e\u5220\u9664\u6210\u529f");
    }

    @PostMapping(value={"/tagUser"})
    public AjaxResult tagUser(@RequestBody Map<String, Object> data) {
        return AjaxResult.success("\u6807\u7b7e\u8bbe\u7f6e\u6210\u529f");
    }

    @GetMapping(value={"/userTags/{userId}"})
    public AjaxResult getUserTags(@PathVariable String userId) {
        ArrayList tags = new ArrayList();
        return AjaxResult.success(tags);
    }

    @Generated
    public WxUserTagsApi() {
    }
}

