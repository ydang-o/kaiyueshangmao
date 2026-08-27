/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.system.domain.SysNotice
 *  com.dingyangmall.system.service.ISysNoticeService
 *  lombok.Generated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.api;

import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.system.domain.SysNotice;
import com.dingyangmall.system.service.ISysNoticeService;
import java.util.List;
import lombok.Generated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/public/ma/notice"})
public class PublicNoticeApi {
    private final ISysNoticeService noticeService;

    @GetMapping(value={"/list"})
    public AjaxResult list() {
        SysNotice query = new SysNotice();
        query.setStatus("0");
        List list = this.noticeService.selectNoticeList(query);
        return AjaxResult.success((Object)list);
    }

    @GetMapping(value={"/type/{noticeType}"})
    public AjaxResult getByType(@PathVariable String noticeType) {
        SysNotice query = new SysNotice();
        query.setNoticeType(noticeType);
        query.setStatus("0");
        List list = this.noticeService.selectNoticeList(query);
        if (list.isEmpty()) {
            return AjaxResult.success(null);
        }
        return AjaxResult.success(list.get(0));
    }

    @Generated
    public PublicNoticeApi(ISysNoticeService noticeService) {
        this.noticeService = noticeService;
    }
}

