/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.system.domain.SysNotice
 *  com.dingyangmall.system.service.ISysNoticeService
 *  lombok.Generated
 *  org.springframework.web.bind.annotation.GetMapping
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/weixin/api/ma/notice"})
public class NoticeApi {
    private final ISysNoticeService noticeService;

    @GetMapping(value={"/list"})
    public AjaxResult list() {
        SysNotice query = new SysNotice();
        query.setStatus("0");
        List list = this.noticeService.selectNoticeList(query);
        return AjaxResult.success((Object)list);
    }

    @Generated
    public NoticeApi(ISysNoticeService noticeService) {
        this.noticeService = noticeService;
    }
}

