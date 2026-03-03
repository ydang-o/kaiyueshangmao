package com.dingyangmall.web.api;

import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.system.domain.SysNotice;
import com.dingyangmall.system.service.ISysNoticeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公开接口：公告列表，不需登录/token。
 * 供首页公告轮播使用，与 /weixin/api/ma/notice/list 返回一致。
 */
@RestController
@RequestMapping("/api/public/ma/notice")
@AllArgsConstructor
public class PublicNoticeApi {

    private final ISysNoticeService noticeService;

    @GetMapping("/list")
    public AjaxResult list() {
        SysNotice query = new SysNotice();
        query.setStatus("0");
        List<SysNotice> list = noticeService.selectNoticeList(query);
        return AjaxResult.success(list);
    }
}
