package com.dingyangmall.web.controller.system;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.entity.OrderInfo;
import com.dingyangmall.mall.entity.OrderItem;
import com.dingyangmall.mall.entity.TbCouponInfo;
import com.dingyangmall.mall.entity.TbIntegralFlow;
import com.dingyangmall.mall.enums.OrderInfoEnum;
import com.dingyangmall.mall.service.GoodsSpuService;
import com.dingyangmall.mall.service.OrderInfoService;
import com.dingyangmall.mall.service.OrderItemService;
import com.dingyangmall.mall.service.TbCouponInfoService;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.UmsMemberService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/system/dashboard")
public class SysDashboardController {
    private final UmsMemberService umsMemberService;
    private final OrderInfoService orderInfoService;
    private final TbCouponInfoService tbCouponInfoService;
    private final TbIntegralFlowService tbIntegralFlowService;
    private final OrderItemService orderItemService;
    private final GoodsSpuService goodsSpuService;

    @GetMapping("/data")
    public AjaxResult getData() { Map<String,Object> d=new HashMap<>(); d.put("userCount",umsMemberService.count()); d.put("pointsIssued",tbIntegralFlowService.count(Wrappers.<TbIntegralFlow>lambdaQuery().gt(TbIntegralFlow::getIntegralNum,0))); d.put("pendingOrders",orderInfoService.count(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getStatus, OrderInfoEnum.STATUS_1.getValue()))); LocalDateTime s=LocalDate.now().atStartOfDay(),e=LocalDate.now().atTime(LocalTime.MAX); d.put("todayWriteOffs",tbCouponInfoService.count(Wrappers.<TbCouponInfo>lambdaQuery().eq(TbCouponInfo::getCouponStatus,2).ge(TbCouponInfo::getUpdateTime,s).le(TbCouponInfo::getUpdateTime,e))); return AjaxResult.success(d); }
    @GetMapping("/statistics/integral") public AjaxResult statisticsIntegral(){Map<String,Object>d=new HashMap<>();List<TbIntegralFlow> fs=tbIntegralFlowService.list();d.put("totalIssued",fs.stream().filter(x->x.getIntegralNum()!=null&&x.getIntegralNum()>0).mapToInt(TbIntegralFlow::getIntegralNum).sum());d.put("flowCount",fs.size());d.put("memberTotalPoints",umsMemberService.list().stream().mapToInt(x->x.getPoints()==null?0:x.getPoints()).sum());return AjaxResult.success(d);}
    @GetMapping("/statistics/order") public AjaxResult statisticsOrder(){Map<String,Object>d=new HashMap<>();d.put("totalOrders",orderInfoService.count());d.put("pendingShip",orderInfoService.count(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getStatus,"1")));d.put("shipped",orderInfoService.count(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getStatus,"2")));d.put("completed",orderInfoService.count(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getStatus,"3")));return AjaxResult.success(d);}
    @GetMapping("/statistics/coupon") public AjaxResult statisticsCoupon(@RequestParam(required=false)Long verifyDealerId){Map<String,Object>d=new HashMap<>();d.put("totalVerified",tbCouponInfoService.count(Wrappers.<TbCouponInfo>lambdaQuery().eq(TbCouponInfo::getCouponStatus,2)));return AjaxResult.success(d);}
    @GetMapping("/statistics/user") public AjaxResult statisticsUser(){Map<String,Object>d=new HashMap<>();d.put("totalMembers",umsMemberService.count());return AjaxResult.success(d);}
    @GetMapping("/statistics/cash-sales") public AjaxResult cashSales(){BigDecimal total=orderInfoService.list().stream().map(OrderInfo::getPaymentPrice).filter(Objects::nonNull).reduce(BigDecimal.ZERO,BigDecimal::add);return AjaxResult.success(Map.of("totalSales",total));}
    @GetMapping("/statistics/goods-sales") public AjaxResult goodsSales(){long total=orderItemService.list().stream().map(OrderItem::getQuantity).filter(Objects::nonNull).mapToLong(Integer::longValue).sum();return AjaxResult.success(Map.of("totalQuantity",total));}
    @GetMapping("/statistics/goods-stock") public AjaxResult goodsStock(){long total=goodsSpuService.list().stream().map(GoodsSpu::getStock).filter(Objects::nonNull).mapToLong(Integer::longValue).sum();return AjaxResult.success(Map.of("totalStock",total));}
    @GetMapping("/statistics/integral-exchange") public AjaxResult integralExchange(){int total=tbIntegralFlowService.list(Wrappers.<TbIntegralFlow>lambdaQuery().eq(TbIntegralFlow::getOperType,1)).stream().map(TbIntegralFlow::getIntegralNum).filter(Objects::nonNull).mapToInt(Math::abs).sum();return AjaxResult.success(Map.of("totalPoints",total));}
    @GetMapping("/statistics/integral-grant") public AjaxResult integralGrant(){int total=tbIntegralFlowService.list().stream().filter(x->x.getIntegralNum()!=null&&x.getIntegralNum()>0).mapToInt(TbIntegralFlow::getIntegralNum).sum();return AjaxResult.success(Map.of("totalPoints",total));}
    @GetMapping("/statistics/member-new") public AjaxResult memberNew(@RequestParam(required=false)String beginTime,@RequestParam(required=false)String endTime){LocalDateTime s=parse(beginTime, true),e=parse(endTime,false);long n=umsMemberService.list().stream().filter(x->x.getCreateTime()!=null&&(s==null||!x.getCreateTime().isBefore(s))&&(e==null||!x.getCreateTime().isAfter(e))).count();return AjaxResult.success(Map.of("count",n));}
    @GetMapping("/statistics/member-referral-detail") public AjaxResult referralDetail(){return AjaxResult.success(tbIntegralFlowService.list(Wrappers.<TbIntegralFlow>lambdaQuery().eq(TbIntegralFlow::getOperType,6).orderByDesc(TbIntegralFlow::getOperTime)));}
    @GetMapping("/statistics/member-referral-summary") public AjaxResult referralSummary(){Map<Long,Integer> counts=new HashMap<>();for(TbIntegralFlow f:tbIntegralFlowService.list(Wrappers.<TbIntegralFlow>lambdaQuery().eq(TbIntegralFlow::getOperType,6))){if(f.getSourceUserId()!=null)counts.merge(f.getSourceUserId(),1,Integer::sum);}return AjaxResult.success(counts);}
    private LocalDateTime parse(String s,boolean start){if(s==null||s.isBlank())return null;try{return s.length()>10?LocalDateTime.parse(s):start?LocalDate.parse(s).atStartOfDay():LocalDate.parse(s).atTime(LocalTime.MAX);}catch(Exception e){return null;}}
}
