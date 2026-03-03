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
 * 小程序端公告列表（README: /weixin/api/ma/notice/list）
 * 用于首页公告轮播，仅返回状态为正常的公告。
 *
 * @author dingyangmall
 */
@RestController
@RequestMapping("/weixin/api/ma/notice")
@AllArgsConstructor
public class NoticeApi {

    private final ISysNoticeService noticeService;

    /**
     * 公告列表（不分页，仅正常状态）
     */
    @GetMapping("/list")
    public AjaxResult list() {
        SysNotice query = new SysNotice();
        query.setStatus("0");
        List<SysNotice> list = noticeService.selectNoticeList(query);
        return AjaxResult.success(list);
    }
}
