package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.mall.entity.TbLotteryConfig;
import com.dingyangmall.mall.entity.TbLotteryRecord;
import com.dingyangmall.mall.service.TbLotteryConfigService;
import com.dingyangmall.mall.service.TbLotteryRecordService;
import com.dingyangmall.mall.utils.MemberUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * C端抽奖接口
 *
 * @author dingyangmall
 * @date 2026-02-13
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/app/lottery")
public class AppLotteryApi {

    private final TbLotteryConfigService lotteryConfigService;
    private final TbLotteryRecordService lotteryRecordService;

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
        
        try {
            Long userId = Long.parseLong(memberIdStr);
            TbLotteryRecord record = lotteryRecordService.draw(userId);
            return AjaxResult.success(record);
        } catch (NumberFormatException e) {
            return AjaxResult.error("无效的用户ID");
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
        
        try {
            Long userId = Long.parseLong(memberIdStr);
            Page<TbLotteryRecord> result = lotteryRecordService.page(page, Wrappers.<TbLotteryRecord>lambdaQuery()
                    .eq(TbLotteryRecord::getUserId, userId)
                    .eq(TbLotteryRecord::getIsWin, "1") // 只显示中奖记录? 还是全部? 通常全部或只中奖. 这里先只显示中奖.
                    // 需求: "抽奖记录链路". DH.txt 10.2 says "Order Statistics", but not specific about lottery record view.
                    // Usually users want to see winning history.
                    .orderByDesc(TbLotteryRecord::getCreateTime));
            return AjaxResult.success(result);
        } catch (NumberFormatException e) {
            return AjaxResult.error("无效的用户ID");
        }
    }
}
