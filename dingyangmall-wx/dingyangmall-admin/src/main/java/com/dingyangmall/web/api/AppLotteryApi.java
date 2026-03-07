package com.dingyangmall.web.api;

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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * C端抽奖接口
 * 小程序 token 中 memberId 为 openid，需解析为 ums_member.id 供抽奖/积分等使用
 *
 * @author dingyangmall
 * @date 2026-02-13
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = { "/app/lottery", "/api/ma/lottery", "/weixin/api/ma/lottery" })
public class AppLotteryApi {

    private final TbLotteryConfigService lotteryConfigService;
    private final TbLotteryRecordService lotteryRecordService;
    private final WxMaUserMapper wxMaUserMapper;
    private final UmsMemberService umsMemberService;

    /**
     * 将 token 中的 memberId（可能为 openid 或数字 id）解析为 ums_member.id
     */
    private Long resolveToUserId(String memberIdStr) {
        if (StringUtils.isEmpty(memberIdStr)) return null;
        try {
            return Long.parseLong(memberIdStr);
        } catch (NumberFormatException e) {
            // memberId 为 openid，通过 wx_user -> ums_member 解析
        }
        WxMaUser wxUser = wxMaUserMapper.selectByOpenid(memberIdStr);
        if (wxUser == null || StringUtils.isEmpty(wxUser.getPhone())) {
            return null;
        }
        UmsMember member = umsMemberService.getOrCreateByPhone(
                wxUser.getPhone(),
                wxUser.getNickname(),
                wxUser.getAvatarUrl());
        return member != null ? member.getId() : null;
    }

    /**
     * 获取抽奖活动信息
     * @return 活动配置及奖品
     */
    @GetMapping("/config")
    public AjaxResult getConfig() {
        TbLotteryConfig config = lotteryConfigService.getActiveConfig();
        if (config == null || "0".equals(config.getStatus())) {
            return AjaxResult.error("当前没有开启的抽奖活动");
        }
        return AjaxResult.success(config);
    }

    /**
     * 参与抽奖
     * @return 抽奖结果
     */
    @PostMapping("/draw")
    public AjaxResult draw() {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return AjaxResult.error("未登录");
        }
        Long userId = resolveToUserId(memberIdStr);
        if (userId == null) {
            return AjaxResult.error("请先绑定手机号");
        }
        try {
            TbLotteryRecord record = lotteryRecordService.draw(userId);
            return AjaxResult.success(record);
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        } catch (Exception e) {
            log.error("抽奖失败", e);
            return AjaxResult.error("抽奖失败，请稍后重试");
        }
    }

    /**
     * 获取我的中奖记录
     * @param page 分页对象
     * @return 记录列表
     */
    @GetMapping("/record")
    public AjaxResult getMyRecords(Page page) {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return AjaxResult.error("未登录");
        }
        Long userId = resolveToUserId(memberIdStr);
        if (userId == null) {
            return AjaxResult.error("请先绑定手机号");
        }
        Page<TbLotteryRecord> result = lotteryRecordService.page(page, Wrappers.<TbLotteryRecord>lambdaQuery()
                .eq(TbLotteryRecord::getUserId, userId)
                .eq(TbLotteryRecord::getIsWin, "1")
                .orderByDesc(TbLotteryRecord::getCreateTime));
        return AjaxResult.success(result);
    }
}
