/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
        if (StringUtils.isEmpty(memberIdStr)) {
            return null;
        }
        try {
            return Long.parseLong(memberIdStr);
        }
        catch (NumberFormatException numberFormatException) {
            WxMaUser wxUser = this.wxMaUserMapper.selectByOpenid(memberIdStr);
            if (wxUser == null || StringUtils.isEmpty(wxUser.getPhone())) {
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
            return AjaxResult.error("\u5f53\u524d\u6ca1\u6709\u5f00\u542f\u7684\u62bd\u5956\u6d3b\u52a8");
        }
        return AjaxResult.success(config);
    }

    @GetMapping(value={"/list"})
    public AjaxResult getList() {
        List<TbLotteryConfig> list = this.lotteryConfigService.getActiveList();
        return AjaxResult.success(list);
    }

    @PostMapping(value={"/draw"})
    public AjaxResult draw() {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return AjaxResult.error("\u672a\u767b\u5f55");
        }
        Long userId = this.resolveToUserId(memberIdStr);
        if (userId == null) {
            return AjaxResult.error("\u8bf7\u5148\u7ed1\u5b9a\u624b\u673a\u53f7");
        }
        try {
            TbLotteryRecord record = this.lotteryRecordService.draw(userId);
            return AjaxResult.success(record);
        }
        catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }
        catch (Exception e) {
            log.error("\u62bd\u5956\u5931\u8d25", e);
            return AjaxResult.error("\u62bd\u5956\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u91cd\u8bd5");
        }
    }

    @GetMapping(value={"/record"})
    public AjaxResult getMyRecords(Page page) {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return AjaxResult.error("\u672a\u767b\u5f55");
        }
        Long userId = this.resolveToUserId(memberIdStr);
        if (userId == null) {
            return AjaxResult.error("\u8bf7\u5148\u7ed1\u5b9a\u624b\u673a\u53f7");
        }
        Page result = this.lotteryRecordService.page(page, (Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbLotteryRecord::getUserId, userId)).eq(TbLotteryRecord::getIsWin, "1")).orderByDesc(TbLotteryRecord::getCreateTime));
        return AjaxResult.success(result);
    }

    @Generated
    public AppLotteryApi(TbLotteryConfigService lotteryConfigService, TbLotteryRecordService lotteryRecordService, WxMaUserMapper wxMaUserMapper, UmsMemberService umsMemberService) {
        this.lotteryConfigService = lotteryConfigService;
        this.lotteryRecordService = lotteryRecordService;
        this.wxMaUserMapper = wxMaUserMapper;
        this.umsMemberService = umsMemberService;
    }
}

