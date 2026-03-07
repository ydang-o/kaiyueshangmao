package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.page.TableDataInfo;
import com.dingyangmall.common.constant.HttpStatus;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.mall.entity.TbCouponInfo;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.TbCouponInfoService;
import com.dingyangmall.mall.service.UmsMemberService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品券管理
 *
 * @author dingyangmall
 * @date 2026-02-12
 */
@RestController
@AllArgsConstructor
@RequestMapping({"/mall/coupon", "/dev-api/mall/coupon"})
public class TbCouponInfoController extends BaseController {

    private final TbCouponInfoService couponInfoService;
    private final UmsMemberService umsMemberService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param tbCouponInfo 商品券信息
     * @return
     */
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermi('mall:coupon:index')")
    public TableDataInfo getPage(Page page, TbCouponInfo tbCouponInfo) {
        com.baomidou.mybatisplus.core.metadata.IPage<TbCouponInfo> result = couponInfoService.page(page, Wrappers.query(tbCouponInfo).lambda().orderByDesc(TbCouponInfo::getCreateTime));
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(result.getRecords());
        rspData.setTotal(result.getTotal());
        return rspData;
    }

    /**
     * 通过id查询商品券信息
     * @param id id
     * @return AjaxResult
     */
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:coupon:get')")
    public AjaxResult getById(@PathVariable("id") Long id) {
        return AjaxResult.success(couponInfoService.getById(id));
    }

    /**
     * 新增商品券信息 (通常由系统自动生成，但也可手动添加)
     * @param tbCouponInfo 商品券信息
     * @return AjaxResult
     */
    @PostMapping
    @PreAuthorize("@ss.hasPermi('mall:coupon:add')")
    public AjaxResult save(@RequestBody TbCouponInfo tbCouponInfo) {
        return toAjax(couponInfoService.save(tbCouponInfo));
    }

    /**
     * 修改商品券信息
     * @param tbCouponInfo 商品券信息
     * @return AjaxResult
     */
    @PutMapping
    @PreAuthorize("@ss.hasPermi('mall:coupon:edit')")
    public AjaxResult update(@RequestBody TbCouponInfo tbCouponInfo) {
        return toAjax(couponInfoService.updateById(tbCouponInfo));
    }

    /**
     * 通过id删除商品券信息
     * @param id id
     * @return AjaxResult
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:coupon:del')")
    public AjaxResult removeById(@PathVariable Long id) {
        return toAjax(couponInfoService.removeById(id));
    }

    /**
     * 发放代金券
     * 请求体：{ "phone": "13800138000", "goodsId": "商品ID", "count": 1, "validityDays": 365 }
     * phone 与 userId 二选一；goodsId 必填；count 默认 1；validityDays 默认 365
     */
    @PostMapping("/distribute")
    @PreAuthorize("@ss.hasPermi('mall:coupon:add')")
    public AjaxResult distribute(@RequestBody Map<String, Object> body) {
        String phone = body != null && body.get("phone") != null ? body.get("phone").toString().trim() : null;
        Long userId = null;
        if (body != null && body.get("userId") != null) {
            Object u = body.get("userId");
            if (u instanceof Number) userId = ((Number) u).longValue();
            else if (u != null) try { userId = Long.parseLong(u.toString()); } catch (NumberFormatException ignored) {}
        }
        if (userId == null && StringUtils.isNotEmpty(phone)) {
            UmsMember member = umsMemberService.getByPhone(phone);
            if (member == null) return AjaxResult.error("用户不存在，请确认手机号已注册");
            userId = member.getId();
        }
        if (userId == null) return AjaxResult.error("请提供手机号(phone)或用户ID(userId)");
        String goodsId = body != null && body.get("goodsId") != null ? body.get("goodsId").toString().trim() : null;
        if (StringUtils.isEmpty(goodsId)) return AjaxResult.error("请选择商品券(goodsId)");
        int count = 1;
        if (body != null && body.get("count") != null) {
            try { count = Integer.parseInt(body.get("count").toString()); } catch (NumberFormatException ignored) {}
        }
        int validityDays = 365;
        if (body != null && body.get("validityDays") != null) {
            try { validityDays = Integer.parseInt(body.get("validityDays").toString()); } catch (NumberFormatException ignored) {}
        }
        try {
            List<TbCouponInfo> list = couponInfoService.distributeCoupon(userId, goodsId, count, validityDays);
            return AjaxResult.success("发放成功，共 " + list.size() + " 张", list);
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 核销统计：总核销量、今日核销、按商家统计（传 verifyDealerId）
     */
    @GetMapping("/statistics")
    @PreAuthorize("@ss.hasPermi('mall:coupon:index')")
    public AjaxResult statistics(@RequestParam(required = false) Long verifyDealerId) {
        var wrapper = com.baomidou.mybatisplus.core.toolkit.Wrappers.<TbCouponInfo>lambdaQuery().eq(TbCouponInfo::getCouponStatus, 2);
        long totalVerified = couponInfoService.count(wrapper);
        java.time.LocalDateTime start = java.time.LocalDateTime.of(java.time.LocalDate.now(), java.time.LocalTime.MIN);
        java.time.LocalDateTime end = java.time.LocalDateTime.of(java.time.LocalDate.now(), java.time.LocalTime.MAX);
        long todayVerified = couponInfoService.count(com.baomidou.mybatisplus.core.toolkit.Wrappers.<TbCouponInfo>lambdaQuery()
                .eq(TbCouponInfo::getCouponStatus, 2)
                .ge(TbCouponInfo::getVerifyTime, start)
                .le(TbCouponInfo::getVerifyTime, end));
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("totalVerified", totalVerified);
        data.put("todayVerified", todayVerified);
        if (verifyDealerId != null) {
            long byDealer = couponInfoService.count(com.baomidou.mybatisplus.core.toolkit.Wrappers.<TbCouponInfo>lambdaQuery()
                    .eq(TbCouponInfo::getCouponStatus, 2)
                    .eq(TbCouponInfo::getVerifyDealerId, verifyDealerId));
            data.put("byDealer", byDealer);
        }
        return AjaxResult.success(data);
    }

}