package com.dingyangmall.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.OrderInfo;
import com.dingyangmall.mall.entity.TbCouponInfo;
import com.dingyangmall.mall.entity.TbIntegralFlow;
import com.dingyangmall.mall.enums.OrderInfoEnum;
import com.dingyangmall.mall.service.OrderInfoService;
import com.dingyangmall.mall.service.TbCouponInfoService;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.UmsMemberService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页仪表盘
 */
@RestController
@AllArgsConstructor
@RequestMapping("/system/dashboard")
public class SysDashboardController {

    private final UmsMemberService umsMemberService;
    private final OrderInfoService orderInfoService;
    private final TbCouponInfoService tbCouponInfoService;
    private final TbIntegralFlowService tbIntegralFlowService;

    @GetMapping("/data")
    public AjaxResult getData() {
        Map<String, Object> data = new HashMap<>();

        // 1. 用户总数
        data.put("userCount", umsMemberService.count());

        // 2. 平台积分发放总量 (这里简单统计所有增加的流水)
        // 注意：这里假设 integralNum > 0 即为发放。如果数据量大，应该使用 SQL sum
        data.put("pointsIssued", tbIntegralFlowService.count(Wrappers.<TbIntegralFlow>lambdaQuery().gt(TbIntegralFlow::getIntegralNum, 0)));

        // 3. 待发货订单
        data.put("pendingOrders", orderInfoService.count(Wrappers.<OrderInfo>lambdaQuery()
                .eq(OrderInfo::getStatus, OrderInfoEnum.STATUS_1.getValue()))); // STATUS_1 is 待发货?
        
        // 4. 今日核销
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        data.put("todayWriteOffs", tbCouponInfoService.count(Wrappers.<TbCouponInfo>lambdaQuery()
                .eq(TbCouponInfo::getCouponStatus, 2) // 2: 已使用
                .ge(TbCouponInfo::getUpdateTime, startOfDay)
                .le(TbCouponInfo::getUpdateTime, endOfDay)));

        return AjaxResult.success(data);
    }

    /** 积分统计：总发放、各一级经销商持有量（此处按会员积分汇总）、流水条数 */
    @GetMapping("/statistics/integral")
    public AjaxResult statisticsIntegral() {
        Map<String, Object> data = new HashMap<>();
        long flowCount = tbIntegralFlowService.count();
        long totalIssue = tbIntegralFlowService.list(Wrappers.<TbIntegralFlow>lambdaQuery().gt(TbIntegralFlow::getIntegralNum, 0))
                .stream().mapToLong(TbIntegralFlow::getIntegralNum).sum();
        data.put("totalIssued", totalIssue);
        data.put("flowCount", flowCount);
        data.put("memberTotalPoints", umsMemberService.list().stream().mapToInt(m -> m.getPoints() != null ? m.getPoints() : 0).sum());
        return AjaxResult.success(data);
    }

    /** 订单统计：订单数、按状态统计 */
    @GetMapping("/statistics/order")
    public AjaxResult statisticsOrder() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalOrders", orderInfoService.count());
        data.put("pendingShip", orderInfoService.count(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getStatus, "1")));
        data.put("shipped", orderInfoService.count(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getStatus, "2")));
        data.put("completed", orderInfoService.count(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getStatus, "3")));
        return AjaxResult.success(data);
    }

    /** 核销统计：总核销数、今日核销 */
    @GetMapping("/statistics/coupon")
    public AjaxResult statisticsCoupon(@RequestParam(required = false) Long verifyDealerId) {
        Map<String, Object> data = new HashMap<>();
        long totalVerified = tbCouponInfoService.count(Wrappers.<TbCouponInfo>lambdaQuery().eq(TbCouponInfo::getCouponStatus, 2));
        data.put("totalVerified", totalVerified);
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        long today = tbCouponInfoService.count(Wrappers.<TbCouponInfo>lambdaQuery()
                .eq(TbCouponInfo::getCouponStatus, 2).ge(TbCouponInfo::getVerifyTime, start).le(TbCouponInfo::getVerifyTime, end));
        data.put("todayVerified", today);
        if (verifyDealerId != null) {
            long byDealer = tbCouponInfoService.count(Wrappers.<TbCouponInfo>lambdaQuery()
                    .eq(TbCouponInfo::getCouponStatus, 2).eq(TbCouponInfo::getVerifyDealerId, verifyDealerId));
            data.put("byDealer", byDealer);
        }
        return AjaxResult.success(data);
    }

    /** 用户统计：总用户数、会员数 */
    @GetMapping("/statistics/user")
    public AjaxResult statisticsUser() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalMembers", umsMemberService.count());
        return AjaxResult.success(data);
    }
}
