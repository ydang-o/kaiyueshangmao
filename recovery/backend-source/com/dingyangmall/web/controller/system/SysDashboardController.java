/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  com.baomidou.mybatisplus.core.toolkit.Wrappers
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.mall.entity.GoodsSpu
 *  com.dingyangmall.mall.entity.OrderInfo
 *  com.dingyangmall.mall.entity.OrderItem
 *  com.dingyangmall.mall.entity.TbCouponInfo
 *  com.dingyangmall.mall.entity.TbIntegralFlow
 *  com.dingyangmall.mall.entity.UmsMember
 *  com.dingyangmall.mall.enums.OrderInfoEnum
 *  com.dingyangmall.mall.service.GoodsSpuService
 *  com.dingyangmall.mall.service.OrderInfoService
 *  com.dingyangmall.mall.service.OrderItemService
 *  com.dingyangmall.mall.service.TbCouponInfoService
 *  com.dingyangmall.mall.service.TbIntegralFlowService
 *  com.dingyangmall.mall.service.UmsMemberService
 *  lombok.Generated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.entity.OrderInfo;
import com.dingyangmall.mall.entity.OrderItem;
import com.dingyangmall.mall.entity.TbCouponInfo;
import com.dingyangmall.mall.entity.TbIntegralFlow;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.enums.OrderInfoEnum;
import com.dingyangmall.mall.service.GoodsSpuService;
import com.dingyangmall.mall.service.OrderInfoService;
import com.dingyangmall.mall.service.OrderItemService;
import com.dingyangmall.mall.service.TbCouponInfoService;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.UmsMemberService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Generated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/system/dashboard"})
public class SysDashboardController {
    private final UmsMemberService umsMemberService;
    private final OrderInfoService orderInfoService;
    private final OrderItemService orderItemService;
    private final GoodsSpuService goodsSpuService;
    private final TbCouponInfoService tbCouponInfoService;
    private final TbIntegralFlowService tbIntegralFlowService;

    @GetMapping(value={"/data"})
    public AjaxResult getData() {
        HashMap<String, Long> data = new HashMap<String, Long>();
        data.put("userCount", this.umsMemberService.count());
        data.put("pointsIssued", this.tbIntegralFlowService.count((Wrapper)Wrappers.lambdaQuery().gt(TbIntegralFlow::getIntegralNum, (Object)0)));
        data.put("pendingOrders", this.orderInfoService.count((Wrapper)Wrappers.lambdaQuery().eq(OrderInfo::getStatus, (Object)OrderInfoEnum.STATUS_1.getValue())));
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        data.put("todayWriteOffs", this.tbCouponInfoService.count((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbCouponInfo::getCouponStatus, (Object)2)).ge(TbCouponInfo::getUpdateTime, (Object)startOfDay)).le(TbCouponInfo::getUpdateTime, (Object)endOfDay)));
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/statistics/integral"})
    public AjaxResult statisticsIntegral() {
        HashMap<String, Number> data = new HashMap<String, Number>();
        long flowCount = this.tbIntegralFlowService.count();
        long totalIssue = this.tbIntegralFlowService.list((Wrapper)Wrappers.lambdaQuery().gt(TbIntegralFlow::getIntegralNum, (Object)0)).stream().mapToLong(TbIntegralFlow::getIntegralNum).sum();
        data.put("totalIssued", totalIssue);
        data.put("flowCount", flowCount);
        data.put("memberTotalPoints", this.umsMemberService.list().stream().mapToInt(m -> m.getPoints() != null ? m.getPoints() : 0).sum());
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/statistics/order"})
    public AjaxResult statisticsOrder() {
        HashMap<String, Long> data = new HashMap<String, Long>();
        data.put("totalOrders", this.orderInfoService.count());
        data.put("pendingShip", this.orderInfoService.count((Wrapper)Wrappers.lambdaQuery().eq(OrderInfo::getStatus, (Object)"1")));
        data.put("shipped", this.orderInfoService.count((Wrapper)Wrappers.lambdaQuery().eq(OrderInfo::getStatus, (Object)"2")));
        data.put("completed", this.orderInfoService.count((Wrapper)Wrappers.lambdaQuery().eq(OrderInfo::getStatus, (Object)"3")));
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/statistics/coupon"})
    public AjaxResult statisticsCoupon(@RequestParam(required=false) Long verifyDealerId) {
        HashMap<String, Long> data = new HashMap<String, Long>();
        long totalVerified = this.tbCouponInfoService.count((Wrapper)Wrappers.lambdaQuery().eq(TbCouponInfo::getCouponStatus, (Object)2));
        data.put("totalVerified", totalVerified);
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        long today = this.tbCouponInfoService.count((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbCouponInfo::getCouponStatus, (Object)2)).ge(TbCouponInfo::getVerifyTime, (Object)start)).le(TbCouponInfo::getVerifyTime, (Object)end));
        data.put("todayVerified", today);
        if (verifyDealerId != null) {
            long byDealer = this.tbCouponInfoService.count((Wrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbCouponInfo::getCouponStatus, (Object)2)).eq(TbCouponInfo::getVerifyDealerId, (Object)verifyDealerId));
            data.put("byDealer", byDealer);
        }
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/statistics/user"})
    public AjaxResult statisticsUser() {
        HashMap<String, Long> data = new HashMap<String, Long>();
        data.put("totalMembers", this.umsMemberService.count());
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/statistics/goods-sales"})
    public AjaxResult statisticsGoodsSales(@RequestParam(required=false) String beginTime, @RequestParam(required=false) String endTime) {
        List paidOrders;
        List paidOrderIds;
        LocalDateTime begin = SysDashboardController.parseTime(beginTime, true);
        LocalDateTime end = SysDashboardController.parseTime(endTime, false);
        LambdaQueryWrapper orderQuery = (LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(OrderInfo::getIsPay, (Object)"1")).ne(OrderInfo::getStatus, (Object)"5");
        if (begin != null) {
            orderQuery.ge(OrderInfo::getPaymentTime, (Object)begin);
        }
        if (end != null) {
            orderQuery.le(OrderInfo::getPaymentTime, (Object)end);
        }
        if ((paidOrderIds = (paidOrders = this.orderInfoService.list((Wrapper)orderQuery)).stream().map(OrderInfo::getId).collect(Collectors.toList())).isEmpty()) {
            HashMap empty = new HashMap();
            empty.put("goodsSalesList", new ArrayList());
            return AjaxResult.success(empty);
        }
        List items = this.orderItemService.list((Wrapper)Wrappers.lambdaQuery().in(OrderItem::getOrderId, paidOrderIds));
        HashMap<String, Map> grouped = new HashMap<String, Map>();
        for (OrderItem item : items) {
            String key = item.getSpuId() + "_" + item.getSpuName();
            Map row = grouped.computeIfAbsent(key, k -> {
                HashMap<String, Object> m = new HashMap<String, Object>();
                m.put("spuId", item.getSpuId());
                m.put("goodsName", item.getSpuName());
                m.put("sold", 0L);
                m.put("salesAmount", BigDecimal.ZERO);
                return m;
            });
            long sold = (Long)row.get("sold") + (long)(item.getQuantity() != null ? item.getQuantity() : 0);
            BigDecimal amount = (BigDecimal)row.get("salesAmount");
            BigDecimal add = item.getPaymentPrice() != null ? item.getPaymentPrice() : BigDecimal.ZERO;
            row.put("sold", sold);
            row.put("salesAmount", amount.add(add));
        }
        ArrayList list = new ArrayList(grouped.values());
        list.sort((a, b) -> Long.compare((Long)b.get("sold"), (Long)a.get("sold")));
        HashMap data = new HashMap();
        data.put("goodsSalesList", list);
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/statistics/goods-stock"})
    public AjaxResult statisticsGoodsStock(@RequestParam(required=false) String beginTime, @RequestParam(required=false) String endTime) {
        List goodsList = this.goodsSpuService.list();
        LocalDateTime begin = SysDashboardController.parseTime(beginTime, true);
        LocalDateTime end = SysDashboardController.parseTime(endTime, false);
        LambdaQueryWrapper itemQuery = Wrappers.lambdaQuery();
        if (begin != null || end != null) {
            List filteredOrderIds;
            LambdaQueryWrapper orderQuery = (LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(OrderInfo::getIsPay, (Object)"1")).ne(OrderInfo::getStatus, (Object)"5");
            if (begin != null) {
                orderQuery.ge(OrderInfo::getPaymentTime, (Object)begin);
            }
            if (end != null) {
                orderQuery.le(OrderInfo::getPaymentTime, (Object)end);
            }
            itemQuery = (filteredOrderIds = this.orderInfoService.list((Wrapper)orderQuery).stream().map(OrderInfo::getId).collect(Collectors.toList())).isEmpty() ? (LambdaQueryWrapper)itemQuery.eq(OrderItem::getOrderId, (Object)"-1") : (LambdaQueryWrapper)itemQuery.in(OrderItem::getOrderId, filteredOrderIds);
        }
        List orderItems = this.orderItemService.list((Wrapper)itemQuery);
        HashMap<String, Integer> soldMap = new HashMap<String, Integer>();
        for (Object item : orderItems) {
            int qty = item.getQuantity() != null ? item.getQuantity() : 0;
            soldMap.merge(item.getSpuId(), qty, Integer::sum);
        }
        ArrayList<Map> list = new ArrayList<Map>();
        for (GoodsSpu g : goodsList) {
            HashMap<String, Object> row = new HashMap<String, Object>();
            row.put("spuId", g.getId());
            row.put("goodsName", g.getName());
            row.put("stock", g.getStock() != null ? g.getStock() : 0);
            row.put("sold", soldMap.getOrDefault(g.getId(), 0));
            list.add(row);
        }
        list.sort(Comparator.comparingInt(o -> (Integer)o.get("stock")));
        HashMap<String, ArrayList<Map>> data = new HashMap<String, ArrayList<Map>>();
        data.put("goodsStockList", list);
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/statistics/cash-sales"})
    public AjaxResult statisticsCashSales(@RequestParam(required=false) String beginTime, @RequestParam(required=false) String endTime) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd");
        ArrayList trend = new ArrayList();
        LocalDateTime begin = SysDashboardController.parseTime(beginTime, true);
        LocalDateTime end = SysDashboardController.parseTime(endTime, false);
        if (begin == null) {
            begin = LocalDateTime.of(LocalDate.now().minusDays(6L), LocalTime.MIN);
        }
        if (end == null) {
            end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        }
        LambdaQueryWrapper query = (LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(OrderInfo::getIsPay, (Object)"1")).ne(OrderInfo::getStatus, (Object)"5")).ge(OrderInfo::getPaymentTime, (Object)begin)).le(OrderInfo::getPaymentTime, (Object)end);
        List paidOrders = this.orderInfoService.list((Wrapper)query);
        LocalDate day = begin.toLocalDate();
        while (!day.isAfter(end.toLocalDate())) {
            LocalDateTime dayStart = LocalDateTime.of(day, LocalTime.MIN);
            LocalDateTime dayEnd = LocalDateTime.of(day, LocalTime.MAX);
            long count = paidOrders.stream().filter(o -> o.getPaymentTime() != null && !o.getPaymentTime().isBefore(dayStart) && !o.getPaymentTime().isAfter(dayEnd)).count();
            BigDecimal amount = paidOrders.stream().filter(o -> o.getPaymentTime() != null && !o.getPaymentTime().isBefore(dayStart) && !o.getPaymentTime().isAfter(dayEnd)).map(o -> o.getPaymentPrice() != null ? o.getPaymentPrice() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            HashMap<String, Object> row = new HashMap<String, Object>();
            row.put("period", day.format(fmt));
            row.put("orderCount", count);
            row.put("cashAmount", amount);
            trend.add(row);
            day = day.plusDays(1L);
        }
        HashMap data = new HashMap();
        data.put("cashSalesTrend", trend);
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/statistics/integral-grant"})
    public AjaxResult statisticsIntegralGrant(@RequestParam(required=false) String beginTime, @RequestParam(required=false) String endTime) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd");
        LocalDateTime begin = SysDashboardController.parseTime(beginTime, true);
        LocalDateTime end = SysDashboardController.parseTime(endTime, false);
        if (begin == null) {
            begin = LocalDateTime.of(LocalDate.now().minusDays(6L), LocalTime.MIN);
        }
        if (end == null) {
            end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        }
        List flows = this.tbIntegralFlowService.list((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().gt(TbIntegralFlow::getIntegralNum, (Object)0)).ge(TbIntegralFlow::getOperTime, (Object)begin)).le(TbIntegralFlow::getOperTime, (Object)end));
        ArrayList trend = new ArrayList();
        LocalDate day = begin.toLocalDate();
        while (!day.isAfter(end.toLocalDate())) {
            LocalDateTime dayStart = LocalDateTime.of(day, LocalTime.MIN);
            LocalDateTime dayEnd = LocalDateTime.of(day, LocalTime.MAX);
            List dayList = flows.stream().filter(f -> f.getOperTime() != null && !f.getOperTime().isBefore(dayStart) && !f.getOperTime().isAfter(dayEnd)).collect(Collectors.toList());
            int points = dayList.stream().mapToInt(f -> f.getIntegralNum() != null ? f.getIntegralNum() : 0).sum();
            HashMap<String, Object> row = new HashMap<String, Object>();
            row.put("period", day.format(fmt));
            row.put("grantCount", dayList.size());
            row.put("grantPoints", points);
            trend.add(row);
            day = day.plusDays(1L);
        }
        HashMap<String, Serializable> data = new HashMap<String, Serializable>();
        data.put("integralGrantTrend", trend);
        data.put("integralGrantTotal", Integer.valueOf(flows.stream().mapToInt(f -> f.getIntegralNum() != null ? f.getIntegralNum() : 0).sum()));
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/statistics/integral-exchange"})
    public AjaxResult statisticsIntegralExchange(@RequestParam(required=false) String beginTime, @RequestParam(required=false) String endTime) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd");
        LocalDateTime begin = SysDashboardController.parseTime(beginTime, true);
        LocalDateTime end = SysDashboardController.parseTime(endTime, false);
        if (begin == null) {
            begin = LocalDateTime.of(LocalDate.now().minusDays(6L), LocalTime.MIN);
        }
        if (end == null) {
            end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        }
        List flows = this.tbIntegralFlowService.list((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().lt(TbIntegralFlow::getIntegralNum, (Object)0)).ge(TbIntegralFlow::getOperTime, (Object)begin)).le(TbIntegralFlow::getOperTime, (Object)end));
        ArrayList trend = new ArrayList();
        LocalDate day = begin.toLocalDate();
        while (!day.isAfter(end.toLocalDate())) {
            LocalDateTime dayStart = LocalDateTime.of(day, LocalTime.MIN);
            LocalDateTime dayEnd = LocalDateTime.of(day, LocalTime.MAX);
            List dayList = flows.stream().filter(f -> f.getOperTime() != null && !f.getOperTime().isBefore(dayStart) && !f.getOperTime().isAfter(dayEnd)).collect(Collectors.toList());
            int points = dayList.stream().mapToInt(f -> Math.abs(f.getIntegralNum() != null ? f.getIntegralNum() : 0)).sum();
            HashMap<String, Object> row = new HashMap<String, Object>();
            row.put("period", day.format(fmt));
            row.put("exchangeCount", dayList.size());
            row.put("exchangePoints", points);
            trend.add(row);
            day = day.plusDays(1L);
        }
        HashMap<String, Serializable> data = new HashMap<String, Serializable>();
        data.put("integralExchangeTrend", trend);
        data.put("integralExchangeTotal", Integer.valueOf(flows.stream().mapToInt(f -> Math.abs(f.getIntegralNum() != null ? f.getIntegralNum() : 0)).sum()));
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/statistics/member-new"})
    public AjaxResult statisticsMemberNew(@RequestParam(required=false) String beginTime, @RequestParam(required=false) String endTime) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd");
        LocalDateTime begin = SysDashboardController.parseTime(beginTime, true);
        LocalDateTime end = SysDashboardController.parseTime(endTime, false);
        if (begin == null) {
            begin = LocalDateTime.of(LocalDate.now().minusDays(6L), LocalTime.MIN);
        }
        if (end == null) {
            end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        }
        List members = this.umsMemberService.list((Wrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().ge(UmsMember::getCreateTime, (Object)begin)).le(UmsMember::getCreateTime, (Object)end));
        ArrayList trend = new ArrayList();
        LocalDate day = begin.toLocalDate();
        while (!day.isAfter(end.toLocalDate())) {
            LocalDateTime dayStart = LocalDateTime.of(day, LocalTime.MIN);
            LocalDateTime dayEnd = LocalDateTime.of(day, LocalTime.MAX);
            long count = members.stream().filter(m -> m.getCreateTime() != null && !m.getCreateTime().isBefore(dayStart) && !m.getCreateTime().isAfter(dayEnd)).count();
            HashMap<String, Object> row = new HashMap<String, Object>();
            row.put("period", day.format(fmt));
            row.put("newMembers", count);
            trend.add(row);
            day = day.plusDays(1L);
        }
        HashMap<String, Serializable> data = new HashMap<String, Serializable>();
        data.put("memberNewTrend", trend);
        data.put("memberTotal", Long.valueOf(this.umsMemberService.count()));
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/statistics/member-referral-detail"})
    public AjaxResult statisticsMemberReferralDetail(@RequestParam(required=false) String beginTime, @RequestParam(required=false) String endTime) {
        LocalDateTime begin = SysDashboardController.parseTime(beginTime, true);
        LocalDateTime end = SysDashboardController.parseTime(endTime, false);
        LambdaQueryWrapper query = (LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbIntegralFlow::getOperType, (Object)6);
        if (begin != null) {
            query.ge(TbIntegralFlow::getOperTime, (Object)begin);
        }
        if (end != null) {
            query.le(TbIntegralFlow::getOperTime, (Object)end);
        }
        List referrals = this.tbIntegralFlowService.list((Wrapper)query);
        Map memberMap = this.umsMemberService.list().stream().collect(Collectors.toMap(UmsMember::getId, Function.identity(), (a, b) -> a));
        ArrayList list = new ArrayList();
        for (TbIntegralFlow flow : referrals) {
            HashMap<String, Object> row = new HashMap<String, Object>();
            UmsMember inviter = (UmsMember)memberMap.get(flow.getSourceUserId());
            UmsMember invitee = (UmsMember)memberMap.get(flow.getUserId());
            row.put("time", flow.getOperTime());
            row.put("inviterId", flow.getSourceUserId());
            row.put("inviterName", inviter != null ? inviter.getNickname() : "-");
            row.put("inviteeId", flow.getUserId());
            row.put("inviteeName", invitee != null ? invitee.getNickname() : "-");
            row.put("rewardPoints", flow.getIntegralNum() != null ? flow.getIntegralNum() : 0);
            list.add(row);
        }
        list.sort((a, b) -> {
            LocalDateTime ta = (LocalDateTime)a.get("time");
            LocalDateTime tb = (LocalDateTime)b.get("time");
            if (ta == null && tb == null) {
                return 0;
            }
            if (ta == null) {
                return 1;
            }
            if (tb == null) {
                return -1;
            }
            return tb.compareTo(ta);
        });
        HashMap data = new HashMap();
        data.put("referralDetails", list);
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/statistics/member-referral-summary"})
    public AjaxResult statisticsMemberReferralSummary(@RequestParam(required=false) String beginTime, @RequestParam(required=false) String endTime) {
        LocalDateTime begin = SysDashboardController.parseTime(beginTime, true);
        LocalDateTime end = SysDashboardController.parseTime(endTime, false);
        LambdaQueryWrapper query = (LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbIntegralFlow::getOperType, (Object)6);
        if (begin != null) {
            query.ge(TbIntegralFlow::getOperTime, (Object)begin);
        }
        if (end != null) {
            query.le(TbIntegralFlow::getOperTime, (Object)end);
        }
        List referrals = this.tbIntegralFlowService.list((Wrapper)query);
        Map memberMap = this.umsMemberService.list().stream().collect(Collectors.toMap(UmsMember::getId, Function.identity(), (a, b) -> a));
        Map<Long, List<TbIntegralFlow>> grouped = referrals.stream().filter(f -> f.getSourceUserId() != null).collect(Collectors.groupingBy(TbIntegralFlow::getSourceUserId));
        ArrayList list = new ArrayList();
        for (Map.Entry<Long, List<TbIntegralFlow>> entry : grouped.entrySet()) {
            Long inviterId = entry.getKey();
            List<TbIntegralFlow> values = entry.getValue();
            UmsMember inviter = (UmsMember)memberMap.get(inviterId);
            HashMap<String, Object> row = new HashMap<String, Object>();
            row.put("inviterId", inviterId);
            row.put("inviterName", inviter != null ? inviter.getNickname() : "-");
            row.put("referralCount", values.size());
            row.put("rewardPoints", values.stream().mapToInt(f -> f.getIntegralNum() != null ? f.getIntegralNum() : 0).sum());
            list.add(row);
        }
        list.sort((a, b) -> Integer.compare((Integer)b.get("referralCount"), (Integer)a.get("referralCount")));
        HashMap data = new HashMap();
        data.put("referralSummary", list);
        return AjaxResult.success(data);
    }

    private static LocalDateTime parseTime(String s, boolean startOfDay) {
        if (s == null || s.isBlank()) {
            return null;
        }
        try {
            if (s.length() > 10) {
                return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            return startOfDay ? LocalDate.parse(s).atStartOfDay() : LocalDate.parse(s).atTime(LocalTime.MAX);
        }
        catch (Exception e) {
            return null;
        }
    }

    @Generated
    public SysDashboardController(UmsMemberService umsMemberService, OrderInfoService orderInfoService, OrderItemService orderItemService, GoodsSpuService goodsSpuService, TbCouponInfoService tbCouponInfoService, TbIntegralFlowService tbIntegralFlowService) {
        this.umsMemberService = umsMemberService;
        this.orderInfoService = orderInfoService;
        this.orderItemService = orderItemService;
        this.goodsSpuService = goodsSpuService;
        this.tbCouponInfoService = tbCouponInfoService;
        this.tbIntegralFlowService = tbIntegralFlowService;
    }
}

