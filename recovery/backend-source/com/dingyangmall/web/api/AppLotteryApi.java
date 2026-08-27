/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.Wrappers
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.common.utils.StringUtils
 *  com.dingyangmall.mall.entity.TbLotteryConfig
 *  com.dingyangmall.mall.entity.TbLotteryRecord
 *  com.dingyangmall.mall.entity.UmsMember
 *  com.dingyangmall.mall.service.TbLotteryConfigService
 *  com.dingyangmall.mall.service.TbLotteryRecordService
 *  com.dingyangmall.mall.service.UmsMemberService
 *  com.dingyangmall.mall.utils.MemberUtils
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.mall.entity.TbLotteryConfig;
import com.dingyangmall.mall.entity.TbLotteryRecord;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.TbLotteryConfigService;
import com.dingyangmall.mall.service.TbLotteryRecordService;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.mall.utils.MemberUtils;
import com.dingyangmall.web.entity.WxMaUser;
import com.dingyangmall.web.mapper.WxMaUserMapper;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/app/lottery", "/api/ma/lottery", "/weixin/api/ma/lottery"})
@CrossOrigin(origins={"*"})
public class AppLotteryApi {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(AppLotteryApi.class);
    private final TbLotteryConfigService lotteryConfigService;
    private final TbLotteryRecordService lotteryRecordService;
    private final WxMaUserMapper wxMaUserMapper;
    private final UmsMemberService umsMemberService;

    private Long resolveToUserId(String memberIdStr) {
        if (StringUtils.isEmpty((String)memberIdStr)) {
            return null;
        }
        try {
            return Long.parseLong(memberIdStr);
        }
        catch (NumberFormatException numberFormatException) {
            WxMaUser wxUser = this.wxMaUserMapper.selectByOpenid(memberIdStr);
            if (wxUser == null || StringUtils.isEmpty((String)wxUser.getPhone())) {
                return null;
            }
            UmsMember member = this.umsMemberService.getOrCreateByPhone(wxUser.getPhone(), wxUser.getNickname(), wxUser.getAvatarUrl());
            return member != null ? member.getId() : null;
        }
    }

    @GetMapping(value={"/config"})
    public AjaxResult getConfig() {
        TbLotteryConfig config = this.lotteryConfigService.getActiveConfig();
        if (config == null || "0".equals(config.getStatus())) {
            return AjaxResult.error((String)"\u5f53\u524d\u6ca1\u6709\u5f00\u542f\u7684\u62bd\u5956\u6d3b\u52a8");
        }
        return AjaxResult.success((Object)config);
    }

    @GetMapping(value={"/list"})
    public AjaxResult getList() {
        List list = this.lotteryConfigService.getActiveList();
        return AjaxResult.success((Object)list);
    }

    @PostMapping(value={"/draw"})
    public AjaxResult draw() {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty((String)memberIdStr)) {
            return AjaxResult.error((String)"\u672a\u767b\u5f55");
        }
        Long userId = this.resolveToUserId(memberIdStr);
        if (userId == null) {
            return AjaxResult.error((String)"\u8bf7\u5148\u7ed1\u5b9a\u624b\u673a\u53f7");
        }
        try {
            TbLotteryRecord record = this.lotteryRecordService.draw(userId);
            return AjaxResult.success((Object)record);
        }
        catch (RuntimeException e) {
            return AjaxResult.error((String)e.getMessage());
        }
        catch (Exception e) {
            log.error("\u62bd\u5956\u5931\u8d25", (Throwable)e);
            return AjaxResult.error((String)"\u62bd\u5956\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u91cd\u8bd5");
        }
    }

    @GetMapping(value={"/record"})
    public AjaxResult getMyRecords(Page page) {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty((String)memberIdStr)) {
            return AjaxResult.error((String)"\u672a\u767b\u5f55");
        }
        Long userId = this.resolveToUserId(memberIdStr);
        if (userId == null) {
            return AjaxResult.error((String)"\u8bf7\u5148\u7ed1\u5b9a\u624b\u673a\u53f7");
        }
        Page result = (Page)this.lotteryRecordService.page((IPage)page, (Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbLotteryRecord::getUserId, (Object)userId)).eq(TbLotteryRecord::getIsWin, (Object)"1")).orderByDesc(TbLotteryRecord::getCreateTime));
        return AjaxResult.success((Object)result);
    }

    @Generated
    public AppLotteryApi(TbLotteryConfigService lotteryConfigService, TbLotteryRecordService lotteryRecordService, WxMaUserMapper wxMaUserMapper, UmsMemberService umsMemberService) {
        this.lotteryConfigService = lotteryConfigService;
        this.lotteryRecordService = lotteryRecordService;
        this.wxMaUserMapper = wxMaUserMapper;
        this.umsMemberService = umsMemberService;
    }
}

